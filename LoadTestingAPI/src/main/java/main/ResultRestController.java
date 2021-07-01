package main;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/result")
public class ResultRestController {

//	@Autowired
//	private JMeterResultsRepository jmRepository;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<JMeterResults> getResultFile() {
		File r = DockerUtils.getResultFile();
		if (r.exists()) {
			JMeterResults jmr = new JMeterResults();
			jmr.loadFile(r);
			return new ResponseEntity<>(jmr, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public ResponseEntity<JMeterResults> getSavedResults() {
		File r = DockerUtils.getResultFile();
		if (r.exists()) {
			JMeterResults jmr = new JMeterResults();
			jmr.loadFile(r);
			return new ResponseEntity<>(jmr, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

//	@RequestMapping(value = "", method = RequestMethod.POST)
//	public ResponseEntity<JMeterResults> saveResult(@RequestBody JMeterResults jmr) {
//		jmRepository.save(jmr);
//		return new ResponseEntity<>(jmr, HttpStatus.OK);
//	}
//	
//	@RequestMapping(value = "", method = RequestMethod.DELETE)
//	public ResponseEntity<JMeterResults> deleteResult(JMeterResults jmr) {
//		jmRepository.delete(jmr);
//		return new ResponseEntity<>(jmr, HttpStatus.OK);
//	}
	
}
