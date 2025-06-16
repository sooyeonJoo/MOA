package com.example.recordservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @brief 통계 정보 DTO
 * @details 카테고리별 지출 통계 정보를 담는 데이터 전송 객체입니다.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatsDTO {
    /** @brief 카테고리 */
    private String category;
    /** @brief 지출 금액 */
    private Integer amount;
    /** @brief 조회 월 (yyyy-MM) */
    private String month;
    /** @brief 총 수입 */
    private Integer income;
} 