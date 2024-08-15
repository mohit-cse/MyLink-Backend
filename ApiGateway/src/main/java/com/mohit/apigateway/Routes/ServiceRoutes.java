package com.mohit.apigateway.Routes;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;


@Configuration
public class ServiceRoutes {
    @Value("${endpoints.user-service}")
    String USER_SERVICE_URL;
    @Value("${endpoints.doc-service}")
    String DOC_SERVICE_URL;
    @Bean
    public RouteLocator serviceRouteLocator(RouteLocatorBuilder builder) {
        return builder
                .routes()
                .route(r -> r.path("/api/auth/**").and().method(HttpMethod.GET)
                        .or().method(HttpMethod.DELETE)
                        .or().method(HttpMethod.POST)
                        .uri("http://" + USER_SERVICE_URL))
                .route(r -> r.path("/api/doc/**").and().method(HttpMethod.GET)
                        .or().method(HttpMethod.DELETE)
                        .or().method(HttpMethod.POST)
                        .uri("http://" + DOC_SERVICE_URL))
                .build();
    }
}
