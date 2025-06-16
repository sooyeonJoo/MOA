package com.example.recordservice.repository;

import com.example.recordservice.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

/**
 * @brief 예산 정보 저장소
 * @details 예산 정보의 영속성을 관리하는 리포지토리입니다.
 */
@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
    /**
     * @brief 사용자 ID와 월로 예산 조회
     * @param userId 사용자 ID
     * @param month 조회할 월 (yyyy-MM)
     * @return 해당 사용자의 해당 월 예산 정보
     */
    Optional<Budget> findByUserIdAndMonth(String userId, YearMonth month);
    /**
     * @brief 사용자 ID로 예산 전체 조회
     * @param userId 사용자 ID
     * @return 해당 사용자의 모든 예산 정보 목록
     */
    List<Budget> findByUserId(String userId);
} 