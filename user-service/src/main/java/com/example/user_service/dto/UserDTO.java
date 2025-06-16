package com.example.user_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * @brief 사용자 정보를 전송하기 위한 DTO 클래스
 * @details OpenAPI 명세서의 User 스키마와 일치하며, 회원가입 등에서 사용됩니다.
 * @author MOA
 * @date 2024-06
 */
@Data
@Schema(description = "사용자 정보 DTO")
public class UserDTO {
    /**
     * @brief 사용자 ID
     * @details 고유한 사용자 식별자입니다.
     */
    @NotBlank(message = "사용자 ID는 필수 입력값입니다")
    @Schema(description = "사용자 ID", example = "joo05", required = true)
    private String userId;

    /**
     * @brief 사용자 이름
     * @details 사용자의 실명 또는 닉네임입니다.
     */
    @NotBlank(message = "사용자 이름은 필수 입력값입니다")
    @Schema(description = "사용자 이름", example = "joo", required = true)
    private String userName;

    /**
     * @brief 비밀번호
     * @details 사용자 계정의 비밀번호입니다.
     */
    @NotBlank(message = "비밀번호는 필수 입력값입니다")
    @Schema(description = "비밀번호", example = "12345", required = true)
    private String password;

    /**
     * @brief 이메일 주소
     * @details 사용자 계정의 이메일 주소입니다.
     */
    @NotBlank(message = "이메일은 필수 입력값입니다")
    @Email(message = "이메일 형식이 올바르지 않습니다")
    @Schema(description = "이메일 주소", example = "john@email.com", required = true)
    private String email;

    /**
     * @brief 전화번호
     * @details 사용자 연락처(숫자만 입력)
     */
    @NotBlank(message = "전화번호는 필수 입력값입니다")
    @Pattern(regexp = "^\\d{10,11}$", message = "전화번호는 10-11자리 숫자만 입력 가능합니다")
    @Schema(description = "전화번호", example = "01024364449", required = true)
    private String tel;
} 