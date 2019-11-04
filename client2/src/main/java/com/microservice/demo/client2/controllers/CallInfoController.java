package com.microservice.demo.client2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.demo.client2.restTypes.TypeOneWithDiscoveryClient;
import com.microservice.demo.client2.restTypes.TypeThreeFeignClient;
import com.microservice.demo.client2.restTypes.TypeTwoRestTemplateClient;

@RestController
@RequestMapping(value = "/call")
public class CallInfoController {
	
    @Autowired
	private TypeOneWithDiscoveryClient typeOneWithDiscoveryClient;
    
    @Autowired
    private TypeTwoRestTemplateClient typeTwoRestTemplateClient;
    
    @Autowired
    private TypeThreeFeignClient typeThreeFeignClient;
	
	
	@RequestMapping(value = "/{callTypeId}", method = RequestMethod.GET)
	public String call(@PathVariable("callTypeId") String callTypeId) {
		
		
		String result = "";

		switch (callTypeId) {
		case "1":
			System.out.println("type one with discovery");
			result = typeOneWithDiscoveryClient.getInfoApp("1");
			break;
		case "2":
			System.out.println("type two with rest template");
			result = typeTwoRestTemplateClient.getInfoApp("2");
			break;
		case "3":
			System.out.println("type three with fiegn");
			result = typeThreeFeignClient.getInfoApp("3");
			break;

		default:
			System.out.println("fiil type");
			break;
		}
		
		return result; 
	}
}
