package com.mdzidko.ordering.customersorders;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
public class CustomerorderApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerorderApplication.class, args);
	}

}
