package com.microservice.demo.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/infos")
public class InfoController {
	
	@RequestMapping(value = "/{infoId}", method = RequestMethod.GET)
	public String getInfoApp(@PathVariable("infoId") String infoId) {
		String result = "";
		if(infoId.equals("1")) {
			result = "bir";
		}else if (infoId.equals("2")) {
			result = "iki";
		}else if (infoId.equals("3")) {
			result = "uc";
		}
		return result; 
	}
}
