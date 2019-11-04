package com.microservice.demo.client2.restTypes;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Utility {
	
	
	public static final String APPLICATION_CLIENT_NAME = "applicationClient";
	public static final String APPLICATION_CLIENT_PORT = "8001";
	
	public static String getHostname() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}

	
}
