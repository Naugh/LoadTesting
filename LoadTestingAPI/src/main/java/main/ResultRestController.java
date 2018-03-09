package main;

import java.io.File;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/result")
public class ResultRestController {
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<JMeterResults> getResultFile(){
	//	DockerUtils.startMaster();
		File r = DockerUtils.getResultFile();
		if(r.exists()){
			JMeterResults jmr = new JMeterResults();
			jmr.loadFile(r);
			return new ResponseEntity<>(jmr, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
}
