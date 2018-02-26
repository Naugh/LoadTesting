package main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
import com.spotify.docker.client.messages.HostConfig;

/*import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateImageCmd;
import com.github.dockerjava.api.model.BuildResponseItem;
import com.github.dockerjava.api.model.Info;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.BuildImageResultCallback;*/

public class DockerUtils {

	private final static String JMETER_BASE = "naughtyth/jmeter-base";
	private final static String JMETER_MASTER = "naughtyth/jmeter-master";
	private final static String JMETER_SLAVE = "naughtyth/jmeter-slave";

	private static DockerClient docker = null;
	// private static Volume volume = null;

	private static String api = null;
	private static String master = null;
	private static ArrayList<String> slaves = null;

	private static InputStream file = null;

	public static void initDocker(int n) {
		if (docker == null) {
			try {
				docker = DefaultDockerClient.fromEnv().build();	
			} catch (DockerCertificateException e) {
				e.printStackTrace();
				System.out.println("DockerUtils.initDocker() error docker client");
			}
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
		if (master == null) {
			try {
				cleanContainers();
				docker.pull(JMETER_BASE);
				docker.pull(JMETER_MASTER);
				master = createContainer(JMETER_MASTER, "jmetermaster", true);
			} catch (DockerException | InterruptedException e) {
				System.out.println("DockerUtils.initDocker() error docker master");
				System.out.println(e.getMessage());
			}
		}
		if (slaves == null) {
			try {
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

	}

	public static String createContainer(String img, String tag) throws DockerException, InterruptedException {
		return createContainer(img, tag, false);
	}

	public static String createContainer(String img, String tag, boolean hasvolume)
			throws DockerException, InterruptedException {
		ContainerConfig config = null;
		if (hasvolume) {
			HostConfig hostConfig = HostConfig.builder().volumesFrom(api).build();
			config = ContainerConfig.builder().image(img).hostConfig(hostConfig).build();
		} else {
			config = ContainerConfig.builder().image(img).build();
		}
		final ContainerCreation container = docker.createContainer(config, tag);
		// docker.startContainer(container.id());
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

	public static InputStream getFile() {
		return file;
	}

	public static void setFile(MultipartFile uploadfile) {
		try {
			File dest = new File("/files/test.jmx");
			uploadfile.transferTo(dest);
		} catch (IOException e) {
			System.out.println("DockerUtils.setFile() error");
			System.out.println(e.getMessage());
		}
	}

	private static void cleanContainers() {
		try {
			List<Container> list = docker.listContainers(ListContainersParam.filter("name", "jmeter*"), ListContainersParam.allContainers());
			if (!list.isEmpty()) {
				for (Container container : list) {
					docker.removeContainer(container.id());
				}
			}
			master = null;
			slaves = null;
		} catch (DockerException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
