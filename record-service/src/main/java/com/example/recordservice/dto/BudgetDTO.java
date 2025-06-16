package com.example.recordservice.dto;

import lombok.*;

/**
 * @brief 예산 DTO
 * @details 월별 예산 정보를 담는 데이터 전송 객체입니다.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BudgetDTO {
    /** @brief 예산 ID */
    private Long budgetId;
    
    /** @brief 사용자 ID */
    private String userId;
    /** @brief 예산 금액 */
    private Integer amount;
    /** @brief 예산 월 (yyyy-MM) */
    private java.time.YearMonth month;
} 