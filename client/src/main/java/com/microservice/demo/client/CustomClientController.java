package com.microservice.demo.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class CustomClientController {

	@Value("${msg : can not retrieve msg ... configserver can not running....}")
	private String simpleStr;

	@GetMapping("/message")
	public String message() {
		return " hello " + simpleStr;
	}
}