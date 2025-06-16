package com.example.recordservice.service;

import com.example.recordservice.dto.BudgetDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

/**
 * @brief 예산 서비스 인터페이스
 * @details 예산 관련 비즈니스 로직을 정의하는 서비스 인터페이스입니다.
 */
@Tag(name = "Budget Service", description = "예산 관리 서비스")
public interface BudgetService {
    /**
     * @brief 예산 등록
     * @param budgetDTO 등록할 예산 정보
     * @return 등록된 예산 정보
     */
    @Operation(summary = "예산 등록", description = "회원의 ID기준으로 예산을 등록한다.")
    BudgetDTO createBudget(
            @Parameter(description = "등록할 예산 내역") BudgetDTO budgetDTO);

    /**
     * @brief 특정 월의 예산 조회
     * @param userId 사용자 ID
     * @return 해당 월의 예산 정보 목록
     */
    @Operation(summary = "월별 예산 조회", description = "특정 월의 예산을 조회합니다.")
    List<BudgetDTO> getMonthlyBudget(
            @Parameter(description = "사용자 ID") String userId);
} 