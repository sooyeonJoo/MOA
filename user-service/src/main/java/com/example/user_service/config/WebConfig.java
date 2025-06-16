package com.example.user_service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * @brief 웹 설정 클래스
 * @details CORS 및 기타 웹 관련 설정을 관리
 * @author MOA
 * @date 2024-06
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * @brief CORS 설정
     * @details Cross-Origin Resource Sharing 설정을 구성
     * @param registry CORS 레지스트리
     * 
     * 설정 내용:
     * 1. 모든 엔드포인트에 대해 CORS 적용
     * 2. 허용할 출처(Origin) 설정
     *    - localhost:8081 (React 개발 서버)
     *    - Swagger UI
     * 3. 허용할 HTTP 메서드 설정
     * 4. 모든 헤더 허용
     * 5. 인증 정보 포함 허용
     * 6. preflight 요청 캐시 시간 설정 (1시간)
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 모든 엔드포인트에 대해 CORS 설정 적용
                .allowedOrigins("http://localhost:8081", "http://localhost:8081/swagger-ui/**","http://localhost:8080")  // React 개발 서버와 Swagger UI
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // 허용할 HTTP 메서드
                .allowedHeaders("*")  // 모든 헤더 허용
                .allowCredentials(true)  // 인증 정보 포함 허용
                .maxAge(3600);  // preflight 요청 캐시 시간 (1시간)
    }
} 