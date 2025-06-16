package com.example.user_service.config;

import com.example.user_service.security.JwtAuthenticationFilter;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

/**
 * @brief Spring Security 설정 클래스
 * @details JWT 인증 및 권한 설정, 보안 정책 정의
 * @author MOA
 * @date 2024-06
 */
@Configuration
@EnableWebSecurity
@Hidden // Swagger 문서에는 노출하지 않음
public class WebSecurityConfig {
    // JWT 인증 필터
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * @brief 생성자
     * @param jwtAuthenticationFilter JWT 인증 필터
     */
    public WebSecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    /**
     * @brief 보안 필터 체인 설정
     * @param http HttpSecurity 객체
     * @return SecurityFilterChain
     * @throws Exception 설정 중 발생할 수 있는 예외
     * @details 1. CSRF 보호 비활성화
     *          2. 세션 정책을 STATELESS로 설정 (JWT 사용)
     *          3. URL별 접근 권한 설정
     *          4. JWT 인증 필터 추가
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // CSRF 보호 비활성화 (REST API이므로)
            .csrf(AbstractHttpConfigurer::disable)
            // 세션을 사용하지 않음 (JWT 사용)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // URL별 접근 권한 설정
            .authorizeHttpRequests(auth -> auth
                // 회원가입, 로그인, Swagger UI는 인증 없이 접근 가능
                .requestMatchers("/user", "/user/login", "/swagger-ui", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                // 그 외 모든 요청은 인증 필요
                .anyRequest().authenticated()
            )
            // JWT 인증 필터 추가
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * @brief 인증 관리자 빈 등록
     * @param authConfig 인증 설정
     * @return AuthenticationManager
     * @throws Exception 설정 중 발생할 수 있는 예외
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * @brief 비밀번호 인코더 빈 등록
     * @return PasswordEncoder
     * @details BCrypt 알고리즘을 사용한 비밀번호 암호화
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
} 