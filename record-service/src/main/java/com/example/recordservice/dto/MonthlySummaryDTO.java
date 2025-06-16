package com.example.recordservice.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * @brief 월별 요약 DTO
 * @details 월별 수입, 지출, 잔고 요약 정보를 담는 데이터 전송 객체입니다.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthlySummaryDTO {
    /** @brief 월 (YYYY-MM) */
    private String month;
    /** @brief 총 수입 */
    private Integer totalIncome;
    /** @brief 총 지출 */
    private Integer totalExpense;
    /** @brief 잔고 */
    private Integer balance;
    private List<RecordSummaryDTO> records;
    private Double budget;
    private LocalDate monthDate;
} 