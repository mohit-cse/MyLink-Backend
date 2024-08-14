package com.mohit.apigateway.Routes;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

@Configuration
public class SwaggerRoutes {
    @Bean
    public RouteLocator swaggerRouteLocator(RouteLocatorBuilder builder) {
        return builder
                .routes()
                .route(r -> r.path("/user-service/v3/api-docs").and().method(HttpMethod.GET)
                        .filters(f->f.rewritePath("/user-service/(?<segment>.*)","/${segment}"))
                        .uri("http://localhost:8081"))
                .route(r -> r.path("/doc-service/v3/api-docs").and().method(HttpMethod.GET)
                        .filters(f->f.rewritePath("/doc-service/(?<segment>.*)","/${segment}"))
                        .uri("http://doc-service-stage.mohit"))
                .build();
    }
}
