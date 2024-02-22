package com.coverflow.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 모든 uri에 대해 특정 도메인은 접근을 허용한다.
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3001", "http://15.165.1.48:3001", "https://coverflow.co.kr")
                .allowedMethods("GET", "POST", "OPTIONS")
                .allowCredentials(true);
    }
}
