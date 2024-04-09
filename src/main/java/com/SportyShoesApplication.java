package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

//@controller, @repository, @service
@SpringBootApplication(scanBasePackages = "com") // by default is scan same package classes as well as sub package of same package.
@EntityScan(basePackages = "com.entity") // @Entity
@EnableJpaRepositories(basePackages ="com.repository") // enable jpa features
public class SportyShoesApplication {

	public static void main(String[] args) {
		SpringApplication.run(SportyShoesApplication.class, args);
		System.out.println("Sporty Shoes up!");
	}

}
