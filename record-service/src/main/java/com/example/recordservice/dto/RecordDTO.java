package com.example.recordservice.dto;

import com.example.recordservice.entity.Record;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * @brief 가계부 기록 DTO
 * @details 사용자의 수입/지출 기록 정보를 담는 데이터 전송 객체입니다.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "가계부 기록 DTO")
public class RecordDTO {
    /** @brief 기록 ID */
    @Schema(description = "기록 ID", example = "1")
    private Long recId;

    /** @brief 사용자 ID */
    @Schema(description = "사용자 ID", example = "user123")
    private String userId;

    /** @brief 금액 */
    @Schema(description = "금액", example = "50000")
    private Integer amount;

    /** @brief 날짜 (yyyy-MM-dd) */
    @Schema(description = "날짜", example = "2024-03-15")
    private LocalDate date;

    /** @brief 카테고리 */
    @Schema(description = "카테고리", example = "식비")
    private String category;

    /** @brief 기록 유형 (수입/지출) */
    @Schema(description = "유형 (INCOME/EXPENSE)", example = "EXPENSE")
    private Record.RecordType type;

    /** @brief 메모 */
    @Schema(description = "메모", example = "점심 식사")
    private String memo;
} 