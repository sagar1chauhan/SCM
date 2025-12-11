package com.scm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScmApplication {

	public static void main(String[] args) {
		System.out.println("BREVO KEY = " + System.getenv("BREVO_API_KEY"));
		SpringApplication.run(ScmApplication.class, args);
		
	}
	

}
