package com.example.user_service.repository;

import com.example.user_service.entity.User;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @brief 사용자 데이터베이스 접근 레포지토리
 * @details User 엔티티에 대한 CRUD 및 사용자 ID, 이메일로 조회 기능 제공
 * @author MOA
 * @date 2024-06
 */
@Repository
@Hidden // Swagger 문서에는 노출하지 않음
public interface UserRepository extends JpaRepository<User, String> {
    /**
     * @brief 사용자 ID로 사용자 조회
     * @param userId 사용자 ID
     * @return Optional<User>
     */
    Optional<User> findByUserId(String userId);

    /**
     * @brief 이메일로 사용자 조회
     * @param email 사용자 이메일
     * @return Optional<User>
     */
    Optional<User> findByEmail(String email);
} 