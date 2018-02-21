package main;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/init")
public class InitRestController {
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public boolean initContainers(@RequestBody int n){
		try {
			DockerUtils.initDocker(n);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
	}
	
	@RequestMapping(value = "/version", method = RequestMethod.GET)
	public boolean get(){
		try {
			DockerUtils.showVersion();
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
	}

}
