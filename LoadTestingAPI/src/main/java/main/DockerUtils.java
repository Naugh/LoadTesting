package main;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerCreation;

/*import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateImageCmd;
import com.github.dockerjava.api.model.BuildResponseItem;
import com.github.dockerjava.api.model.Info;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.BuildImageResultCallback;*/

public class DockerUtils {

	private final static String JMETER_MASTER = "naughtyth/jmeter-master";
	private final static String JMETER_SLAVE = "naughtyth/jmeter-slave";

	private static DockerClient docker = null;

	private static ArrayList<String> slaves = null;
	private static String master = null;

	private static InputStream file = null;

	public static void initDocker(int n) {
		if (docker == null) {
			try {
				docker = DefaultDockerClient.fromEnv().build();
			} catch (DockerCertificateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("DockerUtils.initDocker() error docker client");
			}
		}
		if (master == null) {
			try {
				docker.pull(JMETER_MASTER);
				master = createContainer(JMETER_MASTER, "jmetermaster");
			} catch (DockerException | InterruptedException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
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
				// TODO Auto-generated catch block
				System.out.println("DockerUtils.initDocker() error docker slave");
				System.out.println(e.getMessage());
			}
		}

	}

	public static String createContainer(String img, String tag) throws DockerException, InterruptedException {

		final ContainerConfig config = ContainerConfig.builder().image(img).build();
		final ContainerCreation container = docker.createContainer(config, tag);
		docker.startContainer(container.id());
		return container.id();
	}

	public static void showVersion() {
		try {
			System.out.println(docker.version().toString());
		} catch (DockerException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("DockerUtils.showVersion() error");
		}
	}

	public static InputStream getFile() {
		return file;
	}

	public static void setFile(InputStream file) {
		// DockerUtils.file = file;
		try {
			docker.copyToContainer(new TarArchiveInputStream(file), master, "/");
		} catch (DockerException | InterruptedException | IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			System.out.println("DockerUtils.setFile() error");
			System.out.println(e.getMessage());
		}
	}

	/*
	 * DockerClient dockerClient = null;
	 * 
	 * public DockerUtils(){ dockerClient =
	 * DockerClientBuilder.getInstance("tcp://localhost:4243/").build(); }
	 * 
	 * public void getInfo(){ Info info = dockerClient.infoCmd().exec();
	 * System.out.print(info); }
	 * 
	 * public void buildImage (String dir){ File file = new File(dir);
	 * BuildImageResultCallback callback = new BuildImageResultCallback(){
	 * 
	 * @Override public void onNext(BuildResponseItem item){
	 * //System.out.println("" + item); super.onNext(item); } };
	 * 
	 * CreateImageCmd id = null; try { id = dockerClient.createImageCmd("base",
	 * new FileInputStream(file)); } catch (FileNotFoundException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); }
	 * System.err.println("id ==> " + id.toString()); }
	 */
}
