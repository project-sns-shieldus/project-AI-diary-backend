package com.restapi.project_AI_diary_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.restapi.project_AI_diary_backend")
public class ProjectAiDiaryBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectAiDiaryBackendApplication.class, args);
	}

}
