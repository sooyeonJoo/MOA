package com.example.recordservice.controller;

import com.example.recordservice.dto.BudgetDTO;
import com.example.recordservice.service.BudgetService;
import io.swagger.v3.oas.annotations.Operation;
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
 * @brief 예산 컨트롤러
 * @details 예산 관련 API 요청을 처리하는 REST 컨트롤러입니다.
 */
@CrossOrigin(
    origins = "http://localhost:8080, http://localhost:8081",
    allowCredentials = "true",
    allowedHeaders = "*",
    methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH,
                RequestMethod.DELETE, RequestMethod.OPTIONS }
)
@RestController
@RequestMapping("/budget")
@RequiredArgsConstructor
@Tag(name = "budget", description = "예산 관리 API")
public class BudgetController {
    private final BudgetService budgetService;

    /**
     * @brief 예산 등록 API
     * @param budgetDTO 등록할 예산 정보
     * @return 등록된 예산 정보
     */
    @PostMapping
    @Operation(summary = "예산 등록 API", description = "JWT 토큰의 사용자 ID로 예산을 등록한다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK - 요청 처리 성공"),
        @ApiResponse(responseCode = "default", description = "Unexpected error - 정의되지 않은 오류")
    })
    public ResponseEntity<BudgetDTO> createBudget(@RequestBody BudgetDTO budgetDTO) {
        // JWT 토큰에서 사용자 ID 추출
        String userId = getCurrentUserId();
        budgetDTO.setUserId(userId);
        return ResponseEntity.ok(budgetService.createBudget(budgetDTO));
    }

    /**
     * @brief 현재 사용자의 예산 조회 API
     * @return 현재 사용자의 예산 정보 목록
     */
    @GetMapping
    @Operation(summary = "내 예산 조회", description = "JWT 토큰의 사용자 ID로 예산을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "404", description = "예산 없음")
    })
    public ResponseEntity<List<BudgetDTO>> getMyBudget() {
        // JWT 토큰에서 사용자 ID 추출
        String userId = getCurrentUserId();
        return ResponseEntity.ok(budgetService.getMonthlyBudget(userId));
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