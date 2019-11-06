package com.microservice.demo.client2.restTypes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TypeTwoRestTemplateClient {
	
//	@LoadBalanced
	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
	
	@Autowired
    private RestTemplate restTemplate;
	
	

	public String getInfoApp(String infoId){
        ResponseEntity<String> restExchange =
                restTemplate.exchange(
                        "http://" + Utility.getHostname()
                                  + ":" 
                        		  + Utility.APPLICATION_CLIENT_PORT 
                        		  + "/v1/infos/{infoId}",
                        HttpMethod.GET,
                        null, String.class, infoId);

        return restExchange.getBody();
    }
}
