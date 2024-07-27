package com.mohit.mylink.Properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "auth-properties")
@Data
public class AuthProperties {
    private long tokenValidity;
}
