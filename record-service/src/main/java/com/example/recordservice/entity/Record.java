package com.example.recordservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

/**
 * @brief 가계부 기록 엔티티
 * @details 사용자의 수입/지출 기록을 저장하는 JPA 엔티티입니다.
 */
@Entity
@Table(name = "record")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Record {
    /** @brief 기록 ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recId;
    
    /** @brief 사용자 ID */
    @Column(nullable = false)
    private String userId;

    /** @brief 금액 */
    @Column(nullable = false)
    private Integer amount;

    /** @brief 날짜 */
    @Column(nullable = false)
    private LocalDate date;

    /** @brief 카테고리 */
    @Column(nullable = false)
    private String category;
    
    /** @brief 기록 유형 (수입/지출) */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RecordType type;
    
    /** @brief 메모 */
    private String memo;
    
    /**
     * @brief 기록 유형 열거형
     * @details 수입(INCOME), 지출(EXPENSE) 구분
     */
    public enum RecordType {
        INCOME,  ///< 수입
        EXPENSE  ///< 지출
    }
} 