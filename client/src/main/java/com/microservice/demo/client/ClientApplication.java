package com.microservice.demo.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class ClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClientApplication.class, args);
	}

}

@RestController
@RefreshScope
class CustomClientController {

	@Value("${msg}")
	private String simpleStr;

	@GetMapping("/message")
	public String message() {
		return " hello " + simpleStr;
	}
}