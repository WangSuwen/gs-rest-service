package com.example.restservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RestServiceApplication {

    @Value(value = "${server.port}")
    private static String port;

    public static String getPort() {
        return port;
    }

	public static void main(String[] args) {
		SpringApplication.run(RestServiceApplication.class, args);
        System.out.println("http://localhost:" + getPort() + "/  启动了......");
	}

}
