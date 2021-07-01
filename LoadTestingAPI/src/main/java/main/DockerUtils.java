package main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.DockerClient.ListContainersParam;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.Container;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerCreation;
import com.spotify.docker.client.messages.ContainerInfo;
import com.spotify.docker.client.messages.HostConfig;

public class DockerUtils {

	private final static String JMETER_BASE = "naughtyth/jmeter-base";
	private final static String JMETER_MASTER = "naughtyth/jmeter-master";
	private final static String JMETER_SLAVE = "naughtyth/jmeter-slave";

	private final static String FILES_PATH_DIR = "/files";
	private final static String INPUT_PATH = FILES_PATH_DIR + "/test.jmx";
	private final static String RESULT_PATH = FILES_PATH_DIR + "/results.csv";
	private final static String LOG_PATH = FILES_PATH_DIR + "/jmeter.log";

	private static DockerClient docker = null;

	private static String api = null;
	private static String master = null;
	private static ArrayList<String> slaves = null;

	private static String ipSlaves = null;

	public static void initDocker(int n) {
		if (docker == null) {
			try {
				docker = DefaultDockerClient.fromEnv().build();
			} catch (DockerCertificateException e) {
				e.printStackTrace();
				System.err.println("DockerUtils.initDocker() error docker client");
			}

		}
		if (api == null) {
			try {
				List<Container> list = docker.listContainers(ListContainersParam.filter("name", "loadtestingAPI"));
				if (!list.isEmpty()) {
					api = list.get(0).id();
					docker.pull(JMETER_BASE);
					docker.pull(JMETER_SLAVE);
					docker.pull(JMETER_MASTER);
				} else
					throw new DockerException("Container API name wrong!!");
			} catch (DockerException | InterruptedException e) {
				System.err.println(e.getMessage());
				System.err.println("DockerUtils.initDocker() error docker api volume update");
			}
		}
		cleanContainers();
		if (slaves == null) {
			try {
				slaves = new ArrayList<String>();
				for (int i = 0; i < n; i++) {
					slaves.add(createContainer(JMETER_SLAVE, "jmeterslave" + i));
				}
			} catch (DockerException | InterruptedException e) {
				System.err.println("DockerUtils.initDocker() error docker slave");
				System.err.println(e.getMessage());
			}
		}
		if (master == null) {
			try {
				master = createContainer(JMETER_MASTER, "jmetermaster");
			} catch (DockerException | InterruptedException e) {
				System.err.println("DockerUtils.initDocker() error docker master");
				System.err.println(e.getMessage());
			}
		}

	}

	public static String createContainer(String img, String tag) throws DockerException, InterruptedException {
		ContainerConfig config = null;
		boolean isMaster = img.equals(JMETER_MASTER);
		if (isMaster) {
			HostConfig hostConfig = HostConfig.builder().volumesFrom(api).build();
			config = ContainerConfig.builder().image(img).env("IPSLAVES=" + ipSlaves).hostConfig(hostConfig).build();
		} else {
			config = ContainerConfig.builder().image(img).build();
		}
		final ContainerCreation container = docker.createContainer(config, tag);
		if (!isMaster) {
			docker.startContainer(container.id());
			addIP(docker.inspectContainer(container.id()).networkSettings().ipAddress());
		}
		return container.id();

	}

	public static void showVersion() {
		try {
			System.out.println(docker.version().toString());
		} catch (DockerException | InterruptedException e) {
			e.printStackTrace();
			System.err.println("DockerUtils.showVersion() error");
		}
	}

	public static void setFile(MultipartFile uploadfile) throws IOException {
		File dest = new File(INPUT_PATH);
		if (dest.exists())
			dest.delete();
		uploadfile.transferTo(dest);
		System.out.println("File transfered");
	}

	private static void cleanContainers() {
		try {
			List<Container> list = docker.listContainers(ListContainersParam.filter("name", "jmeter*"),
					ListContainersParam.allContainers());
			if (!list.isEmpty()) {
				for (Container container : list) {
					docker.stopContainer(container.id(), 0);
					docker.removeContainer(container.id());
				}
			}
			master = null;
			slaves = null;
			ipSlaves = null;
		} catch (DockerException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void showIPs() {
		try {
			for (String id : slaves) {
				ContainerInfo info = docker.inspectContainer(id);
				System.err.println(info.networkSettings().ipAddress());
			}
		} catch (DockerException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String showPath() {
		File curDir = new File(FILES_PATH_DIR);
		File[] filesList = curDir.listFiles();
		if (filesList.length == 0) {
			return FILES_PATH_DIR + "dir is empty";
		}
		String names = "Directory: " + FILES_PATH_DIR + "\n";
		for (File f : filesList) {
			/*
			 * if(f.isDirectory()) getAllFiles(f); if(f.isFile()){
			 */
			names += f.getName() + "\n";
			// }
		}
		return names;

	}

	private static void addIP(String ip) {
		if (ipSlaves == null) {
			ipSlaves = ip;
		} else {
			ipSlaves = ipSlaves + "," + ip;
		}
	}

	public static void startMaster() {
		try {
			System.out.println("Processing file");
			docker.startContainer(master);
			docker.waitContainer(master);
			System.out.println("File processed");
		} catch (DockerException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void waitMaster() {
		try {
			docker.waitContainer(master);
		} catch (DockerException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static File getResultFile() {
		File f = new File(RESULT_PATH);
		if (f.exists()) {
			f.delete();
		}
		startMaster();
		return f;
	}
	
	public static String getJMeterLog() {
		File f = new File(LOG_PATH);
		if (f.exists()) {
			try {
				return new String(Files.readAllBytes(Paths.get(LOG_PATH)));
			} catch (IOException e) {
				e.printStackTrace();
				return "Reading file error";
			}
		}else
			return "jmeter.log not found";
	}
}
