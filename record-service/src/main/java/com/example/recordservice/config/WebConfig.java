package com.example.recordservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @brief 웹 설정 클래스
 * @details CORS 및 기타 웹 관련 설정을 관리
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * @brief CORS 설정
     * @details Cross-Origin Resource Sharing 설정을 구성
     * @param registry CORS 레지스트리
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8082", "http://localhost:8082/swagger-ui/**", "http://localhost:8080")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
} 