package main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.ListContainersCmd;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.VolumesFrom;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;

public class DockerUtils {

	private final static String JMETER_BASE = "naughtyth/jmeter-base";
	private final static String JMETER_MASTER = "naughtyth/jmeter-master";
	private final static String JMETER_SLAVE = "naughtyth/jmeter-slave";
	
	private final static String FILES_PATH_DIR = "/files";
	private final static String INPUT_PATH = FILES_PATH_DIR + "/test.jmx";
	private final static String RESULT_PATH = FILES_PATH_DIR + "/results.csv";

	private static DockerClient docker = null;

	private static String api = null;
	private static String master = null;
	private static ArrayList<String> slaves = null;
	
	private static String ipSlaves = null;

	public static void initDocker(int n) {


		if (docker == null) {
			try {
				DockerClientConfig standard = DefaultDockerClientConfig.createDefaultConfigBuilder().build();
				DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
					    .dockerHost(standard.getDockerHost())
					    .sslConfig(standard.getSSLConfig())	
					    .maxConnections(100)
					    .build();	
				
				docker = DockerClientImpl.getInstance(standard, httpClient);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("DockerUtils.initDocker() error docker client");
			}

		}
		if (api == null) {
			try {
				ListContainersCmd list = docker.listContainersCmd().withNameFilter(Arrays.asList("loadtestingAPI"));
//				List<Container> list = docker.listContainers(ListContainersParam.filter("name", "loadtestingAPI"));
				
				if (!list.exec().isEmpty())
					api = list.exec().get(0).getId();
				else
					throw new Exception("Something with API wrong!");
			} catch (Exception e) {
				System.out.println(e.getMessage());
				System.out.println("DockerUtils.initDocker() error docker api volume update");
			}
		}
		if (slaves == null) {
			try {
				cleanContainers();
				docker.pullImageCmd(JMETER_BASE);
				docker.pullImageCmd(JMETER_SLAVE);
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
				docker.pullImageCmd(JMETER_MASTER);
				//TODO 
				//quitar el if api -TEST-
				if (api != null)
					master = createContainer(JMETER_MASTER, "jmetermaster");
				else
					master = createContainer(JMETER_MASTER, "jmetermaster");
			} catch (DockerException | InterruptedException e) {
				System.out.println("DockerUtils.initDocker() error docker master");
				System.out.println(e.getMessage());
			}
		}

	}

//	public static String createContainer(String img, String tag) throws DockerException, InterruptedException {
//		return createContainer(img, tag, false);
//	}

	public static String createContainer(String img, String tag)
			throws DockerException, InterruptedException {
		
		boolean isMaster = img.equals(JMETER_MASTER);

//		ContainerConfig config = null;
		
		if (isMaster) {
//		if(img.equals(JMETER_MASTER)){
//			docker.createContainerCmd(JMETER_MASTER);
//			HostConfig hostConfig = HostConfig.builder().volumesFrom(api).build();
//			config = ContainerConfig.builder().image(img).env("IPSLAVES="+ipSlaves).hostConfig(hostConfig).build();
//			
//			HostConfig hostConfig = HostConfig.newHostConfig().withVolumesFrom(new VolumesFrom(api));
//			docker.createContainerCmd(JMETER_MASTER).withEnv("IPSLAVES="+ipSlaves);
			HostConfig hostConfig = HostConfig.newHostConfig().withVolumesFrom(new VolumesFrom(api));
			CreateContainerResponse container
			  = docker.createContainerCmd(JMETER_MASTER)
//			    .withCmd("--bind_ip_all")
//			    .withName("mongo")
//			    .withHostName("baeldung")
			    .withEnv("IPSLAVES="+ipSlaves)
//			    .withPortBindings(PortBinding.parse("9999:27017"))
//			    .withBinds(Bind.parse("/Users/baeldung/mongo/data/db:/data/db"))
			    .withHostConfig(hostConfig)
			    .exec();
			return container.getId();
			
		} else {
			CreateContainerResponse container = docker.createContainerCmd(JMETER_SLAVE).exec();
			addIP(docker.inspectContainerCmd(container.getId()).exec().getNetworkSettings().getIpAddress());
//			addIP(docker.inspectContainerCmd(container.getId()));
			return container.getId();
		}
//		final ContainerCreation container = docker.createContainer(config, tag);createContainerCmd
//		if(!isMaster){
//			docker.startContainer(container.id());
//			addIP(docker.inspectContainer(container.id()).networkSettings().ipAddress());
//		}
//		return container.id();

	}

	public static void showVersion() {
		try {
			System.out.println(docker.versionCmd().toString());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("DockerUtils.showVersion() error");
		}
	}

	public static void setFile(MultipartFile uploadfile) throws IOException{
		File dest = new File(INPUT_PATH);
		if(dest.exists())
			dest.delete();
		uploadfile.transferTo(dest);
		System.out.println("File transfered");
	}

	private static void cleanContainers() {
		try {
			List<Container> list = docker.listContainersCmd().withNameFilter(Arrays.asList("jmeter*")).exec();
			if (!list.isEmpty()) {
				for (Container container : list) {
					docker.stopContainerCmd(container.getId()).exec();
					docker.removeContainerCmd(container.getId()).exec();
				}
			}
			master = null;
			slaves = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void showIPs() {
		try {
			for (String id : slaves) {
				System.out.println(docker.inspectContainerCmd(id).exec().getNetworkSettings().getIpAddress());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String showPath(){
			File curDir = new File(FILES_PATH_DIR);
			File[] filesList = curDir.listFiles();
			if(filesList.length == 0) {
				return FILES_PATH_DIR + "dir is empty";
			}
			String names = "Directory: " + FILES_PATH_DIR + "\n";
			for (File f : filesList) {
				/*
				 * if(f.isDirectory()) getAllFiles(f); if(f.isFile()){
				 */
				names+=f.getName()+"\n";
				// }
			}
			return names;

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
			System.out.println("Processing file");
			docker.startContainerCmd(master).exec();
			docker.waitContainerCmd(master).exec(null);
			System.out.println("File processed");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void waitMaster(){
		try {
			docker.waitContainerCmd(master).exec(null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static File getResultFile(){
		File f = new File(RESULT_PATH);
		if(f.exists()){
			f.delete();
		}
		startMaster();
		return f;
	}
	
}
