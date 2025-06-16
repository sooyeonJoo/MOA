package com.example.recordservice.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.time.LocalDate;
import java.time.YearMonth;

/**
 * @brief YearMonth <-> LocalDate 변환기
 * @details JPA에서 YearMonth 타입을 LocalDate로 변환하여 DB에 저장/조회할 수 있도록 하는 AttributeConverter입니다.
 */
@Converter(autoApply = true)
public class YearMonthConverter implements AttributeConverter<YearMonth, LocalDate> {
    /**
     * @brief YearMonth를 LocalDate로 변환
     * @param yearMonth 변환할 YearMonth
     * @return 변환된 LocalDate (해당 월의 1일)
     */
    @Override
    public LocalDate convertToDatabaseColumn(YearMonth yearMonth) {
        return yearMonth != null ? yearMonth.atDay(1) : null;
    }

    /**
     * @brief LocalDate를 YearMonth로 변환
     * @param localDate 변환할 LocalDate
     * @return 변환된 YearMonth
     */
    @Override
    public YearMonth convertToEntityAttribute(LocalDate localDate) {
        return localDate != null ? YearMonth.from(localDate) : null;
    }
} 