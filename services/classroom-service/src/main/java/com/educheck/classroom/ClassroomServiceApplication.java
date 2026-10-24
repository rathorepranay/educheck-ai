package com.educheck.classroom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableMethodSecurity
public class ClassroomServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClassroomServiceApplication.class, args);
	}

}
