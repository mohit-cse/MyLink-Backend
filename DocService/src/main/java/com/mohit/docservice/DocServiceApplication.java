package com.mohit.docservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SpringBootApplication
public class DocServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DocServiceApplication.class, args);
	}

}
