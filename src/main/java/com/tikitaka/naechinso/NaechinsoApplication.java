package com.tikitaka.naechinso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing //jpa entity 자동 감시
public class NaechinsoApplication {

	public static void main(String[] args) {
		SpringApplication.run(NaechinsoApplication.class, args);
	}
}
