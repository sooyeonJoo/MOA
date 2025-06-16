package com.example.recordservice.controller;

import com.example.recordservice.dto.StatsDTO;
import com.example.recordservice.service.StatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @brief 통계 컨트롤러
 * @details 통계 관련 API 요청을 처리하는 REST 컨트롤러입니다.
 */
@RestController
@RequestMapping("/stats")
@RequiredArgsConstructor
@Tag(name = "Statistics", description = "통계 정보 API")
public class StatsController {
    private final StatsService statsService;

    /**
     * @brief 월별 통계 조회 API
     * @param month 조회할 월 (yyyy-MM)
     * @return 카테고리별 지출 합계 정보
     */
    @GetMapping
    @Operation(summary = "내 통계 조회 API", description = "JWT 토큰의 사용자 ID와 날짜 기준으로 통계를 조회한다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK - 요청 처리 성공"),
        @ApiResponse(responseCode = "default", description = "Unexpected error - 정의되지 않은 오류")
    })
    public ResponseEntity<List<StatsDTO>> getMyStats(
            @Parameter(description = "월 (YYYY-MM)") @RequestParam(name = "month") String month) {
        // JWT 토큰에서 사용자 ID 추출
        String userId = getCurrentUserId();
        return ResponseEntity.ok(statsService.getStatsById(userId, month));
    }

    /**
     * @brief JWT 토큰에서 현재 사용자 ID 추출
     * @return 현재 사용자 ID
     */
    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName(); // JWT에서 추출된 사용자 ID
    }
} 