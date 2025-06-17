package com.example.user_service.config;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @brief JWT 관련 설정 클래스
 * @details JWT 시크릿, 만료시간 등 설정값을 관리
 * @author MOA
 * @date 2024-06
 */
@Configuration
@ConfigurationProperties(prefix = "jwt")
@Hidden // Swagger 문서에는 노출하지 않음
public class JwtConfig {
    /**
     * @brief JWT 시크릿 키
     */
    private String jwtSecret;
    /**
     * @brief JWT 만료 시간 (ms)
     */
    private long jwtExpirationInMs;

    public String getJwtSecret() {
        return jwtSecret;
    }
    public void setJwtSecret(String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }
    public long getJwtExpirationInMs() {
        return jwtExpirationInMs;
    }
    public void setJwtExpirationInMs(long jwtExpirationInMs) {
        this.jwtExpirationInMs = jwtExpirationInMs;
    }
} 