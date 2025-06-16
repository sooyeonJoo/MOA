package com.example.user_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @brief 로그인 응답 DTO
 * @details JWT 토큰 반환용 DTO, OpenAPI 명세서의 inline_response_200 스키마와 일치
 * @author MOA
 * @date 2024-06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "로그인 응답 DTO")
public class LoginResponseDTO {
    /**
     * @brief JWT 액세스 토큰
     * @details 로그인 성공 시 반환되는 JWT 토큰입니다.
     */
    @Schema(description = "JWT 액세스 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...", required = true)
    private String accessToken;

    @Schema(description = "사용자 ID", example = "user123", required = true)
    private String userId;
} 