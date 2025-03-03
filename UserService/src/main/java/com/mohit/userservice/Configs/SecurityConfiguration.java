package com.mohit.userservice.Configs;

import com.mohit.userservice.Filters.JWTAuthenticationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;
import java.util.logging.Logger;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private final AuthenticationProvider authenticationProvider;
    private final JWTAuthenticationFilter jwtAuthenticationFilter;
    @Value("${security-config.allowed-paths}")
    private List<String> allowedPaths;
    @Value("${security-config.cors-config.allowed-origins}")
    private List<String> allowedCORSOrigins;
    @Value("${security-config.cors-config.allowed-methods}")
    private List<String> allowedCORSMethods;
    @Value("${security-config.cors-config.allowed-headers}")
    private List<String> allowedCORSHeaders;
    @Value("${security-config.cors-config.registered-paths}")
    private List<String> registeredCORSPaths;

    public SecurityConfiguration(
            JWTAuthenticationFilter jwtAuthenticationFilter,
            AuthenticationProvider authenticationProvider
    ) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers(allowedPaths.toArray(new String[0])).permitAll()
                                .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(allowedCORSOrigins);
        configuration.setAllowedMethods(allowedCORSMethods);
        configuration.setAllowedHeaders(allowedCORSHeaders);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        for(String path: registeredCORSPaths)
            source.registerCorsConfiguration(path, configuration);
        return source;
    }
}