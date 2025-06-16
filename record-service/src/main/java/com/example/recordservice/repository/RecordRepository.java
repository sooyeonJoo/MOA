package com.example.recordservice.repository;

import com.example.recordservice.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

/**
 * @brief 가계부 기록 저장소
 * @details 가계부 기록의 영속성을 관리하는 리포지토리입니다.
 */
@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {
    /**
     * @brief 특정 기간 동안의 사용자 가계부 기록 조회
     * @param userId 사용자 ID
     * @param startDate 조회 시작일
     * @param endDate 조회 종료일
     * @return 해당 기간 동안의 가계부 기록 목록
     */
    List<Record> findByUserIdAndDateBetween(String userId, LocalDate startDate, LocalDate endDate);

    /**
     * @brief 사용자의 모든 가계부 기록 조회
     * @param userId 사용자 ID
     * @return 사용자의 모든 가계부 기록 목록
     */
    List<Record> findByUserId(String userId);
} 