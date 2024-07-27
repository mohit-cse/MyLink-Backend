package com.mohit.mylink;

import com.mohit.mylink.Properties.AuthProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@ConfigurationPropertiesScan("com.mohit.mylink.Properties")
public class MylinkApplication {

	public static void main(String[] args) {
		SpringApplication.run(MylinkApplication.class, args);
	}

}
