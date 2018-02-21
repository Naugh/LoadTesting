package main;

import java.io.IOException;

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
		//InputStream inputStream =  new BufferedInputStream(file.getInputStream());
		// logger.debug("Single file upload!");

		if (uploadfile.isEmpty()) {
			// return new ResponseEntity("please select a file!",
			// HttpStatus.OK);
			return false;
		}

		try {
			DockerUtils.setFile(uploadfile.getInputStream());
			// saveUploadedFiles(Arrays.asList(uploadfile));

		} catch (IOException e) {
			// return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			return false;
		}

		// return new ResponseEntity("Successfully uploaded - " +
		// uploadfile.getOriginalFilename(), new HttpHeaders(), HttpStatus.OK);
		return true;

	}
}
