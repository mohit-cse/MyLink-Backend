package com.mohit.mylink.Properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "auth-properties")
@ConfigurationPropertiesScan
@PropertySource("classpath:auth.properties")
@Data
public class AuthProperties {
    private long tokenValidity;
}
