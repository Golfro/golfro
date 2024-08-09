package com.itwill.golfro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class GolfroApplication {

	public static void main(String[] args) {
		SpringApplication.run(GolfroApplication.class, args);
	}

}
