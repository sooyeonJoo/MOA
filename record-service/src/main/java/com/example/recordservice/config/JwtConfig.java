package com.example.recordservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @brief JWT 설정 클래스
 * @details JWT 관련 설정값을 관리합니다.
 */
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {
    private String jwtSecret;
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