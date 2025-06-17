package com.example.recordservice.service.impl;

import com.example.recordservice.dto.StatsDTO;
import com.example.recordservice.entity.Record;
import com.example.recordservice.repository.RecordRepository;
import com.example.recordservice.service.StatsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @brief 통계 서비스 구현체
 * @details StatsService 인터페이스의 구현체로, 통계 관련 비즈니스 로직을 처리합니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatsServiceImpl implements StatsService {
    private final RecordRepository recordRepository;

    /**
     * @brief 월별 통계 정보 조회
     * @param userId 사용자 ID
     * @param month 조회할 월 (yyyy-MM)
     * @return 카테고리별 지출 합계 정보
     */
    @Override
    public List<StatsDTO> getStatsById(String userId, String month) {
        YearMonth ym = YearMonth.parse(month);
        LocalDate start = ym.atDay(1);
        LocalDate end = ym.atEndOfMonth();

        List<Record> records = recordRepository.findByUserIdAndDateBetween(userId, start, end);

        log.debug("Stats query - userId: {}, start: {}, end: {}, records count: {}", 
                  userId, start, end, records.size());
        
        if (log.isTraceEnabled()) {
            records.forEach(r -> log.trace("Record - id: {}, date: {}, type: {}, category: {}, amount: {}", 
                    r.getRecId(), r.getDate(), r.getType(), r.getCategory(), r.getAmount()));
        }

        int totalIncome = records.stream()
            .filter(r -> r.getType() == Record.RecordType.INCOME)
            .mapToInt(Record::getAmount)
            .sum();

        Map<String, Integer> expenseByCategory = records.stream()
            .filter(r -> r.getType() == Record.RecordType.EXPENSE)
            .collect(Collectors.groupingBy(
                Record::getCategory,
                Collectors.summingInt(Record::getAmount)
            ));

        return expenseByCategory.entrySet().stream()
            .map(entry -> StatsDTO.builder()
                .category(entry.getKey())
                .amount(entry.getValue())
                .month(month)
                .income(totalIncome)
                .build())
            .collect(Collectors.toList());
    }
} 