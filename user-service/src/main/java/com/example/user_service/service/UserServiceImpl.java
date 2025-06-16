package com.example.user_service.service;

import com.example.user_service.dto.LoginRequestDTO;
import com.example.user_service.dto.LoginResponseDTO;
import com.example.user_service.dto.UserDTO;
import com.example.user_service.entity.User;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @brief 사용자 서비스 구현체
 * @details UserService의 실제 구현, 회원가입/로그인/로그아웃 로직 담당
 * @author MOA
 * @date 2024-06
 */
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    public UserServiceImpl(UserRepository userRepository,
                         PasswordEncoder passwordEncoder,
                         JwtTokenProvider jwtTokenProvider,
                         AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
    }

    @Override
    @Transactional
    public UserDTO registerUser(UserDTO userDTO) {
        // 1. 중복 ID 체크
        if (userRepository.findByUserId(userDTO.getUserId()).isPresent()) {
            throw new RuntimeException("이미 존재하는 아이디입니다.");
        }

        // 2. 중복 이메일 체크
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }

        // 3. 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());

        // 4. User 엔티티 생성
        User user = User.builder()
                .userId(userDTO.getUserId())
                .userName(userDTO.getUserName())
                .password(encodedPassword)
                .email(userDTO.getEmail())
                .tel(userDTO.getTel())
                .build();

        // 5. DB 저장
        User savedUser = userRepository.save(user);

        // 6. DTO로 변환하여 반환 (비밀번호 제외)
        return convertToDTO(savedUser);
    }

    @Override
    public LoginResponseDTO loginUser(LoginRequestDTO loginRequestDTO) {
        try {
            // 1. 인증 시도
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequestDTO.getUserId(),
                    loginRequestDTO.getPassword()
                )
            );

            // 2. SecurityContext에 인증 정보 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 3. JWT 토큰 생성
            String token = jwtTokenProvider.createToken(loginRequestDTO.getUserId());

            // 4. 응답 DTO 생성 및 반환
            return new LoginResponseDTO(token, loginRequestDTO.getUserId());
        } catch (Exception e) {
            throw new RuntimeException("로그인에 실패했습니다: " + e.getMessage());
        }
    }

    @Override
    public void logoutUser() {
        // SecurityContext 초기화
        SecurityContextHolder.clearContext();
    }

    /**
     * @brief User 엔티티를 UserDTO로 변환
     * @param user User 엔티티
     * @return UserDTO (비밀번호 제외)
     */
    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setUserId(user.getUserId());
        dto.setUserName(user.getUserName());
        dto.setEmail(user.getEmail());
        dto.setTel(user.getTel());
        return dto;
    }
} 