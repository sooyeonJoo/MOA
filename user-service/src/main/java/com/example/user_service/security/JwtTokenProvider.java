package com.example.user_service.security;

import com.example.user_service.config.JwtConfig;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * @brief JWT 토큰 생성 및 검증 클래스
 * @details JWT 토큰의 생성, 파싱, 검증 기능 제공
 * @author MOA
 * @date 2024-06
 */
@Component
@Hidden
public class JwtTokenProvider {
    // JWT 설정 정보 (시크릿 키, 만료 시간 등)
    private final JwtConfig jwtConfig;
    // JWT 서명에 사용할 시크릿 키
    private final SecretKey key;

    /**
     * @brief 생성자
     * @param jwtConfig JWT 설정 정보
     * @details 설정된 시크릿 키를 바이트 배열로 변환하여 SecretKey 생성
     */
    public JwtTokenProvider(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
        this.key = Keys.hmacShaKeyFor(jwtConfig.getJwtSecret().getBytes(StandardCharsets.UTF_8));
    }

    /**
     * @brief JWT 토큰 생성
     * @param userId 사용자 ID
     * @return JWT 토큰 문자열
     * @details 1. 현재 시간과 만료 시간 설정
     *          2. 사용자 ID를 주제(subject)로 설정
     *          3. HS256 알고리즘으로 서명
     */
    public String createToken(String userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtConfig.getJwtExpirationInMs());

        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * @brief JWT 토큰에서 사용자 ID 추출
     * @param token JWT 토큰
     * @return 사용자 ID
     * @details 1. 토큰 파싱
     *          2. 클레임에서 subject(사용자 ID) 추출
     */
    public String getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    /**
     * @brief JWT 토큰 유효성 검증
     * @param token JWT 토큰
     * @return 유효하면 true, 아니면 false
     * @details 1. 토큰 파싱 시도
     *          2. 파싱 성공하면 유효한 토큰
     *          3. 예외 발생하면 유효하지 않은 토큰
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
} 