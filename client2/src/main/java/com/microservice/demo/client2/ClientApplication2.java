package com.microservice.demo.client2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class ClientApplication2 {

	public static void main(String[] args) {
		SpringApplication.run(ClientApplication2.class, args);
	}

}