package com.example.recordservice.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.YearMonth;

/**
 * @brief 예산 엔티티
 * @details 사용자의 월별 예산 정보를 저장하는 JPA 엔티티입니다.
 */
@Entity
@Table(name = "budget")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Budget {
    /** @brief 예산 ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "budget_id")
    private Long id;
    
    /** @brief 사용자 ID */
    @Column(nullable = false)
    private String userId;

    /** @brief 예산 금액 */
    @Column(nullable = false)
    private Integer amount;

    /** @brief 예산 월 (yyyy-MM) */
    @Column(nullable = false)
    @Convert(converter = YearMonthConverter.class)
    private YearMonth month;

    public void updateAmount(Integer amount) {
        this.amount = amount;
    }
} 