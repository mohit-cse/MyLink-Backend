package com.mohit.apigateway.Routes;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;


@Configuration
public class ServiceRoutes {
    @Bean
    public RouteLocator serviceRouteLocator(RouteLocatorBuilder builder) {
        return builder
                .routes()
                .route(r -> r.path("/api/auth/**").and().method(HttpMethod.GET)
                        .or().method(HttpMethod.DELETE)
                        .or().method(HttpMethod.POST)
                        .uri("http://user-service-stage.mohit"))
                .route(r -> r.path("/api/doc/**").and().method(HttpMethod.GET)
                        .or().method(HttpMethod.DELETE)
                        .or().method(HttpMethod.POST)
                        .uri("http://doc-service-stage.mohit"))
                .build();
    }
}
