package main;

import java.io.File;
import java.io.IOException;
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
	//private final static String JMETER_MASTER = "naughtyth/jmeter-master";
	private final static String JMETER_MASTER = "jmeter-master";
	private final static String JMETER_SLAVE = "naughtyth/jmeter-slave";

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
				System.out.println("DockerUtils.initDocker() error docker client");
			}

		}
		if (api == null) {
			try {
				List<Container> list = docker.listContainers(ListContainersParam.filter("name", "loadtestingAPI"));
				if (!list.isEmpty())
					api = list.get(0).id();
				else
					throw new DockerException("Container API name wrong!!");
			} catch (DockerException | InterruptedException e) {
				System.out.println(e.getMessage());
				System.out.println("DockerUtils.initDocker() error docker api volume update");
			}
		}
		if (slaves == null) {
			try {
				cleanContainers();
				docker.pull(JMETER_BASE);
				docker.pull(JMETER_SLAVE);
				slaves = new ArrayList<String>();
				for (int i = 0; i < n; i++) {
					slaves.add(createContainer(JMETER_SLAVE, "jmeterslave" + i));
				}
			} catch (DockerException | InterruptedException e) {
				System.out.println("DockerUtils.initDocker() error docker slave");
				System.out.println(e.getMessage());
			}
		}
		if (master == null) {
			try {
				//docker.pull(JMETER_MASTER);
				//TODO 
				//quitar el if api -TEST-
				if (api != null)
					master = createContainer(JMETER_MASTER, "jmetermaster", true);
				else
					master = createContainer(JMETER_MASTER, "jmetermaster", false);
			} catch (DockerException | InterruptedException e) {
				System.out.println("DockerUtils.initDocker() error docker master");
				System.out.println(e.getMessage());
			}
		}

	}

	public static String createContainer(String img, String tag) throws DockerException, InterruptedException {
		return createContainer(img, tag, false);
	}

	public static String createContainer(String img, String tag, boolean isMaster)
			throws DockerException, InterruptedException {
		ContainerConfig config = null;
		if (isMaster) {
			HostConfig hostConfig = HostConfig.builder().volumesFrom(api).build();
			config = ContainerConfig.builder().image(img).env("IPSlaves="+ipSlaves).hostConfig(hostConfig).build();
		} else {
			config = ContainerConfig.builder().image(img).build();
		}
		final ContainerCreation container = docker.createContainer(config, tag);
		if(!isMaster){
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
			System.out.println("DockerUtils.showVersion() error");
		}
	}

	public static void setFile(MultipartFile uploadfile) {
		try {
			File dest = new File("/files/test.jmx");
			if(dest.exists())
				dest.delete();
			uploadfile.transferTo(dest);
		} catch (IOException e) {
			System.out.println("DockerUtils.setFile() error");
			System.out.println(e.getMessage());
		}
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
	
	public static void showPath(){
			File curDir = new File("/files");

			File[] filesList = curDir.listFiles();
			for (File f : filesList) {
				/*
				 * if(f.isDirectory()) getAllFiles(f); if(f.isFile()){
				 */
				System.out.println(f.getName());
				// }
			}

	}
	
	private static void addIP(String ip){
		if(ipSlaves==null){
			ipSlaves = ip;
		}else{
			ipSlaves=ipSlaves + "," + ip;
		}
	}
	
	public static void startMaster(){
		try {
			docker.startContainer(master);
		} catch (DockerException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
