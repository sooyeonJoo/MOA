package com.example.user_service.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @brief 사용자 엔티티
 * @details DB에 저장되는 사용자 정보, OpenAPI User 스키마와 일치
 * @author MOA
 * @date 2024-06
 */
@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "사용자 엔티티")
public class User {

    /**
     * @brief 사용자 ID
     * @details 고유한 사용자 식별자
     */
    @Id
    @Column(unique = true, nullable = false)
    @Schema(description = "사용자 ID", example = "joo05", required = true)
    private String userId;

    /**
     * @brief 사용자 이름
     * @details 사용자의 실명 또는 닉네임
     */
    @Column(nullable = false)
    @Schema(description = "사용자 이름", example = "joo", required = true)
    private String userName;

    /**
     * @brief 비밀번호
     * @details 사용자 계정의 비밀번호
     */
    @Column(nullable = false)
    @Schema(description = "비밀번호", example = "12345", required = true)
    private String password;

    /**
     * @brief 이메일 주소
     * @details 사용자 계정의 이메일 주소
     */
    @Column(unique = true, nullable = false)
    @Schema(description = "이메일 주소", example = "john@email.com", required = true)
    private String email;

    /**
     * @brief 전화번호
     * @details 사용자 연락처(숫자만 입력)
     */
    @Column(nullable = false)
    @Schema(description = "전화번호", example = "01012345678", required = true)
    private String tel;
} 