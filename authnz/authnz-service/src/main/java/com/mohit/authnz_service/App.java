package com.mohit.authnz_service;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@OpenAPIDefinition(info=@Info(title="Authnz service", version="1.0.0", description="Authentication and Authorization Service for the application"))
public class App {
    public static void main( String[] args ) {
        SpringApplication.run(App.class, args);
    }
}
