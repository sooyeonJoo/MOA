package com.example.user_service.controller;
import com.example.user_service.service.UserService; 

import lombok.RequiredArgsConstructor;
import com.example.user_service.dto.LoginRequestDTO;
import com.example.user_service.dto.LoginResponseDTO;
import com.example.user_service.dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @brief 사용자 관련 API 컨트롤러
 * @details 회원가입, 로그인, 로그아웃 기능 제공 (OAS 명세 기반)
 * @author MOA
 * @date 2024-06
 */

@CrossOrigin(origins = "http://localhost:8080/*")
@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
@Tag(name = "user", description = "회원가입")
public class UserController {
    private final UserService userService;
    /**
     * @brief 회원가입 API
     * @details 회원가입을 진행한다.
     * @param userDTO 가입할 회원의 정보
     * @return 생성된 회원 정보
     */
    @Operation(summary = "회원가입API", description = "회원가입을 진행한다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "회원 생성"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "409", description = "이미 존재하는 아이디"),
        @ApiResponse(responseCode = "422", description = "데이터 유효성 검사 실패"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
        UserDTO createdUser = userService.registerUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    /**
     * @brief 로그인 API
     * @details 회원이 시스템에 로그인한다.
     * @param loginRequestDTO 로그인 할 회원정보
     * @return JWT 토큰
     */
    @Operation(summary = "로그인 API", description = "회원이 시스템에 로그인한다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "로그인 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> loginUser(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        LoginResponseDTO response = userService.loginUser(loginRequestDTO);
        return ResponseEntity.ok(response);
    }

    /**
     * @brief 로그아웃 API
     * @details 회원이 시스템에서 로그아웃한다.
     */
    @Operation(summary = "로그아웃 API", description = "회원이 시스템에서 로그아웃한다.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "로그아웃 성공"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @DeleteMapping("/logout")
    public ResponseEntity<Void> logoutUser() {
        userService.logoutUser();
        return ResponseEntity.noContent().build();
    }
} 