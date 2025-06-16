package com.example.recordservice.service;

import com.example.recordservice.dto.StatsDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

/**
 * @brief 통계 서비스 인터페이스
 * @details 통계 관련 비즈니스 로직을 정의하는 서비스 인터페이스입니다.
 */
@Tag(name = "Stats Service", description = "통계 정보 제공 서비스")
public interface StatsService {
    /**
     * @brief 월별 통계 정보 조회
     * @param userId 사용자 ID
     * @param month 조회할 월 (yyyy-MM)
     * @return 카테고리별 지출 합계 정보
     */
    @Operation(summary = "통계 조회 API", description = "회원의 ID와 날짜 기준으로 통계를 조회한다.")
    List<StatsDTO> getStatsById(
            @Parameter(description = "사용자 ID") String userId,
            @Parameter(description = "월 (YYYY-MM)") String month);
} 