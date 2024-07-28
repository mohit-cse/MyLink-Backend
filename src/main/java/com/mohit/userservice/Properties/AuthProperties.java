package com.mohit.userservice.Properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.PropertySource;

@ConfigurationProperties(prefix = "auth-properties")
@ConfigurationPropertiesScan
@PropertySource("classpath:auth.properties")
@Data
public class AuthProperties {
    private long tokenValidity;
}
