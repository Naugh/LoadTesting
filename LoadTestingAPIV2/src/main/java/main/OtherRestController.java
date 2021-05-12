package main;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
public class OtherRestController {
	
	
	@RequestMapping(value = "/version", method = RequestMethod.GET)
	public boolean getVersion(){
		try {
			DockerUtils.showVersion();
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
	}
	
	@RequestMapping(value = "/info", method = RequestMethod.GET)
	public boolean getInfo(){
		try {

			DockerUtils.showIPs();
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
	}

}
