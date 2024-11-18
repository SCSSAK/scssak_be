package com.example.scsa_community2.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("https://scsa.duckdns.org", "http://localhost:8080", "http://localhost:3000") // TODO : 프론트 도메인으로 변경
                .allowedMethods("GET", "POST", "PUT", "DELETE","OPTION")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
