package com.tikitaka.naechinso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling //scheduler 사용
@EnableJpaAuditing //jpa entity 자동 감시
@SpringBootApplication
public class NaechinsoApplication {
	public static void main(String[] args) {
		SpringApplication.run(NaechinsoApplication.class, args);
	}
}
