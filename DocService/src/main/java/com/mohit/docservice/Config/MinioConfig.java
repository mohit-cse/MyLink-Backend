package com.mohit.docservice.Config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {
    @Value("${spring.data.minio.node}")
    private String node;
    @Value("${spring.data.minio.user}")
    private String user;
    @Value("${spring.data.minio.password}")
    private String password;
    @Bean
    MinioClient createMinioClient(){
        return MinioClient.builder()
                .endpoint(node)
                .credentials(user, password)
                .build();
    }
}
