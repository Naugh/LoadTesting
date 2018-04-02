package main;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
public class FileRestController {

	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile uploadfile) {
		// InputStream inputStream = new
		// BufferedInputStream(file.getInputStream());
		// logger.debug("Single file upload!");

		if (uploadfile.isEmpty()) {
			// return new ResponseEntity("please select a file!",
			// HttpStatus.OK);
			return new ResponseEntity<String>("There is not a file!", HttpStatus.BAD_REQUEST);
		}

		// DockerUtils.setFile(uploadfile.getInputStream());
		DockerUtils.setFile(uploadfile);
		// saveUploadedFiles(Arrays.asList(uploadfile));

		// return new ResponseEntity("Successfully uploaded - " +
		// uploadfile.getOriginalFilename(), new HttpHeaders(), HttpStatus.OK);
		return new ResponseEntity<String>(uploadfile.getName() + " uploaded correctly", HttpStatus.OK);

	}

	/*@RequestMapping(value = "", method = RequestMethod.GET)
	public boolean uploadFile() {
		// InputStream inputStream = new
		// BufferedInputStream(file.getInputStream());
		// logger.debug("Single file upload!");
		File curDir = new File("files");

		// DockerUtils.setFile(uploadfile.getInputStream());
		// saveUploadedFiles(Arrays.asList(uploadfile));

		// return new ResponseEntity("Successfully uploaded - " +
		// uploadfile.getOriginalFilename(), new HttpHeaders(), HttpStatus.OK);
		return true;

	}*/
	
	@RequestMapping(value = "/path", method = RequestMethod.GET)
	public void showPath(){
		DockerUtils.showPath();
	}

}
