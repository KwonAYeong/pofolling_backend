package com.kkks.pofolling;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class PofollingBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(PofollingBackendApplication.class, args);
	}

}
