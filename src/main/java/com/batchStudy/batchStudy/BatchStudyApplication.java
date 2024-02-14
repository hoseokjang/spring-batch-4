package com.batchStudy.batchStudy;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class BatchStudyApplication {

	public static void main(String[] args) {
		SpringApplication.run(BatchStudyApplication.class, args);
	}

}
