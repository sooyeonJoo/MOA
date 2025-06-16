package com.example.user_service.security;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.lang.NonNull;

import java.io.IOException;

/**
 * @brief JWT 인증 필터
 * @details HTTP 요청에서 JWT 토큰을 추출 및 검증하여 인증 처리
 * @author MOA
 * @date 2024-06
 */
@Component
@Hidden // Swagger 문서에는 노출하지 않음
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    // JWT 토큰 처리를 위한 의존성 주입
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    /**
     * @brief 생성자
     * @param jwtTokenProvider JWT 토큰 생성/검증을 담당하는 클래스
     * @param userDetailsService 사용자 정보를 로드하는 서비스
     */
    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    /**
     * @brief HTTP 요청 필터링 처리
     * @details 1. 요청에서 JWT 토큰 추출
     *          2. 토큰 유효성 검증
     *          3. 유효한 토큰이면 사용자 인증 처리
     * @param request HTTP 요청
     * @param response HTTP 응답
     * @param filterChain 필터 체인
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, 
                                  @NonNull HttpServletResponse response, 
                                  @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // 1. 요청 헤더에서 JWT 토큰 추출
            String jwt = getJwtFromRequest(request);

            // 2. 토큰이 존재하고 유효한 경우
            if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
                // 3. 토큰에서 사용자 ID 추출
                String userId = jwtTokenProvider.getUserIdFromToken(jwt);
                // 4. 사용자 정보 로드
                UserDetails userDetails = userDetailsService.loadUserByUsername(userId);

                // 5. 인증 토큰 생성 및 SecurityContext에 저장
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            // 6. 인증 처리 중 오류 발생 시 로깅
            logger.error("Could not set user authentication in security context", ex);
        }

        // 7. 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }

    /**
     * @brief HTTP 요청 헤더에서 JWT 토큰 추출
     * @param request HTTP 요청
     * @return JWT 토큰 문자열 (Bearer 접두사 제외)
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        // "Bearer " 접두사가 있는 경우 제거하고 토큰 반환
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
} 