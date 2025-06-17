package com.example.recordservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @brief 웹 설정 클래스
 * @details CORS 및 기타 웹 관련 설정을 관리
 */
@Configuration
@ConfigurationProperties(prefix = "cors")
@Data
public class WebConfig implements WebMvcConfigurer {

    private List<String> allowedOrigins;
    private List<String> allowedMethods;
    private String allowedHeaders;
    private boolean allowCredentials;

    /**
     * @brief CORS 설정
     * @details Cross-Origin Resource Sharing 설정을 profile별로 구성
     * @param registry CORS 레지스트리
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(allowedOrigins.toArray(new String[0]))  // profile별 설정에서 가져옴
                .allowedMethods(allowedMethods.toArray(new String[0]))  // profile별 메서드 설정
                .allowedHeaders(allowedHeaders)  // 모든 헤더 허용
                .allowCredentials(allowCredentials)  // 인증 정보 포함 허용
                .maxAge(3600);
    }
} 