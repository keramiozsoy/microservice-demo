package com.microservice.demo.client3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;


@EnableEurekaClient
@SpringBootApplication
public class ClientApplication3 {
	
	public static void main(String[] args) {
		SpringApplication.run(ClientApplication3.class, args);
	}

}