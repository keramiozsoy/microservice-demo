package com.microservice.demo.client2.restTypes;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TypeOneWithDiscoveryClient {

	@Autowired
	private DiscoveryClient discoveryClient;
	
	  public String getInfoApp(String infoId) {
	        RestTemplate restTemplate = new RestTemplate();
	        List<ServiceInstance> instances = discoveryClient.getInstances(Utility.APPLICATION_CLIENT_NAME);

	        if (instances.size()==0) 
	        	return null;
	        
	        String serviceUri = String.format("%s/v1/infos/%s",instances.get(0).getUri().toString(), infoId);
	    
	        ResponseEntity< String > restExchange =
	                restTemplate.exchange(
	                        serviceUri,
	                        HttpMethod.GET,
	                        null, String.class, infoId);

	        return restExchange.getBody();
	    }
	
	
}
