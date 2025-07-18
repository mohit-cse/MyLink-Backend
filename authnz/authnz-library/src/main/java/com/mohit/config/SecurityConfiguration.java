package com.mohit.config;

import com.mohit.filters.AuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@EnableMethodSecurity
@Configuration
public class SecurityConfiguration {
    private final AuthenticationFilter authenticationFilter;

    public SecurityConfiguration(AuthenticationFilter authenticationFilter){
        this.authenticationFilter = authenticationFilter;
    }

    @Bean
    static GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults("");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.addFilterBefore(authenticationFilter, AuthorizationFilter.class)
//                .authorizeHttpRequests(
//                        matcher ->
//                                matcher
//                                        .requestMatchers(
//                                                "/swagger-ui.html",
//                                                "/swagger-ui/*",
//                                                "/v3/api-docs",
//                                                "/v3/api-docs/swagger-config")
//                                        .permitAll())
//                .authorizeHttpRequests(
//                        matcher ->
//                                matcher
//                                        // method security will be evaluated after DSL configs,
//                                        // so we have to define public paths upfront
//                                        .requestMatchers(HttpMethod.POST, "/api/auth/login", "/api/users")
//                                        .permitAll())
                .authorizeHttpRequests(matcher -> matcher.anyRequest().authenticated())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(
                        configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//                .exceptionHandling(
//                        customizer ->
//                                customizer
//                                        .accessDeniedHandler(accessDeniedHandler)
//                                        .authenticationEntryPoint(authenticationEntryPoint));

        return http.build();
    }

    @Bean
    public AuthenticationManager noOpAuthenticationManager() {
        return authentication -> null;
    }
}
