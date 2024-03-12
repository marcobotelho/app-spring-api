package com.projeto.appspringapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:custom.properties")
public class AppSpringApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppSpringApiApplication.class, args);
	}

}
