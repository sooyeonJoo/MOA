package com.example.user_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @brief 로그인 요청 DTO
 * @details OpenAPI 명세서의 LoginRequest 스키마와 일치하며, 로그인 요청에 사용됩니다.
 * @author MOA
 * @date 2024-06
 */
@Data
@Schema(description = "로그인 요청 DTO")
public class LoginRequestDTO {
    /**
     * @brief 사용자 ID
     * @details 로그인에 사용되는 사용자 식별자입니다.
     */
    @NotBlank(message = "사용자 ID는 필수 입력값입니다")
    @Schema(description = "사용자 ID", example = "joo05", required = true)
    private String userId;

    /**
     * @brief 비밀번호
     * @details 로그인에 사용되는 비밀번호입니다.
     */
    @NotBlank(message = "비밀번호는 필수 입력값입니다")
    @Schema(description = "비밀번호", required = true)
    private String password;
} 