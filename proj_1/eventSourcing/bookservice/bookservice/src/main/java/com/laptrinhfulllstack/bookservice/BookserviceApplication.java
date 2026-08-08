package com.laptrinhfulllstack.bookservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EnableDiscoveryClient
@EntityScan(basePackages = "com.laptrinhfulllstack.bookservice")
public class BookserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookserviceApplication.class, args);
	}

}
