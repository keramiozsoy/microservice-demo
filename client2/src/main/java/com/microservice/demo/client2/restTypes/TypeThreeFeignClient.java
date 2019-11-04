package com.microservice.demo.client2.restTypes;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(Utility.APPLICATION_CLIENT_NAME)
public interface TypeThreeFeignClient {
	
	@RequestMapping(
            method= RequestMethod.GET,
            value="/v1/infos/{infoId}",
            consumes="application/json")
    String getInfoApp(@PathVariable("infoId") String infoId);
	
}
