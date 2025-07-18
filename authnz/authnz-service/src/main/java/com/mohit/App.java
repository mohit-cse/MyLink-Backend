package com.mohit;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@OpenAPIDefinition(info=@Info(title="Authnz service API", version = "1.0", description = "Service for authentication and authorization management"))
public class App {
    public static void main( String[] args ) {
        SpringApplication.run(App.class, args);
    }
}
