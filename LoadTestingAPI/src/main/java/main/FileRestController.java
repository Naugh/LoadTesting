package main;

import java.io.File;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
public class FileRestController {

	@RequestMapping(value = "", method = RequestMethod.POST)
	public boolean uploadFile(@RequestParam("file") MultipartFile uploadfile) {
		// InputStream inputStream = new
		// BufferedInputStream(file.getInputStream());
		// logger.debug("Single file upload!");

		if (uploadfile.isEmpty()) {
			// return new ResponseEntity("please select a file!",
			// HttpStatus.OK);
			return false;
		}

		// DockerUtils.setFile(uploadfile.getInputStream());
		DockerUtils.setFile(uploadfile);
		// saveUploadedFiles(Arrays.asList(uploadfile));

		// return new ResponseEntity("Successfully uploaded - " +
		// uploadfile.getOriginalFilename(), new HttpHeaders(), HttpStatus.OK);
		return true;

	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public boolean uploadFile() {
		// InputStream inputStream = new
		// BufferedInputStream(file.getInputStream());
		// logger.debug("Single file upload!");
		File curDir = new File("files");
		getAllFiles(curDir);

		// DockerUtils.setFile(uploadfile.getInputStream());
		// saveUploadedFiles(Arrays.asList(uploadfile));

		// return new ResponseEntity("Successfully uploaded - " +
		// uploadfile.getOriginalFilename(), new HttpHeaders(), HttpStatus.OK);
		return true;

	}

	private static void getAllFiles(File curDir) {

		File[] filesList = curDir.listFiles();
		for (File f : filesList) {
			/*
			 * if(f.isDirectory()) getAllFiles(f); if(f.isFile()){
			 */
			System.out.println(f.getName());
			// }
		}

	}
}
