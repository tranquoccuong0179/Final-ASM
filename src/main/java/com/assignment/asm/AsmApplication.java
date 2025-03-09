package com.assignment.asm;

import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableProcessApplication
public class AsmApplication {

	public static void main(String[] args) {
		SpringApplication.run(AsmApplication.class, args);
	}

}
