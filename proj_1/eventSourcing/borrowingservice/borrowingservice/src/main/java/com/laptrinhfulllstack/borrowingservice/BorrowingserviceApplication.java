package com.laptrinhfulllstack.borrowingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@EnableDiscoveryClient
@ComponentScan({ "com.laptrinhfulllstack.borrowingservice", "com.laptrinhfulllstack.commonservice" })
@SpringBootApplication
public class BorrowingserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BorrowingserviceApplication.class, args);
	}

}
