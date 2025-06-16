package com.example.user_service.service;

import com.example.user_service.dto.LoginRequestDTO;
import com.example.user_service.dto.LoginResponseDTO;
import com.example.user_service.dto.UserDTO;

/**
 * @brief 사용자 서비스 인터페이스
 * @details 회원가입, 로그인, 로그아웃 등 사용자 관련 비즈니스 로직 정의
 * @author MOA
 * @date 2024-06
 */
public interface UserService {
    /**
     * @brief 회원가입 처리
     * @param userDTO 회원가입 정보
     * @return 생성된 회원 정보
     */
    UserDTO registerUser(UserDTO userDTO);

    /**
     * @brief 로그인 처리
     * @param loginRequestDTO 로그인 요청 정보
     * @return JWT 토큰
     */
    LoginResponseDTO loginUser(LoginRequestDTO loginRequestDTO);

    /**
     * @brief 로그아웃 처리
     */
    void logoutUser();
} 