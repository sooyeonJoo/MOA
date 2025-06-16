package com.example.recordservice.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * @brief 기록 요약 DTO
 * @details 카테고리별 또는 기간별 기록 요약 정보를 담는 데이터 전송 객체입니다.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecordSummaryDTO {
    /** @brief 카테고리 */
    private Integer amount;
    private LocalDate date;
    private String type;  // "INCOME" or "EXPENSE"
} 