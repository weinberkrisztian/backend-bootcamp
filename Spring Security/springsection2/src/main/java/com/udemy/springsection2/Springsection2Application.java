package com.udemy.springsection2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.udemy.springsection2.repository")
@EntityScan("com.udemy.springsection2.entity")
public class Springsection2Application {

	public static void main(String[] args) {
		SpringApplication.run(Springsection2Application.class, args);
	}

}
