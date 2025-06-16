package com.example.recordservice.service.impl;

import com.example.recordservice.dto.BudgetDTO;
import com.example.recordservice.entity.Budget;
import com.example.recordservice.repository.BudgetRepository;
import com.example.recordservice.service.BudgetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @brief 예산 서비스 구현체
 * @details BudgetService 인터페이스의 구현체로, 예산 관련 비즈니스 로직을 처리합니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BudgetServiceImpl implements BudgetService {
    private final BudgetRepository budgetRepository;

    /**
     * @brief 예산 등록
     * @param budgetDTO 등록할 예산 내역
     * @return 등록된 예산 정보
     */
    @Override
    @Transactional
    public BudgetDTO createBudget(BudgetDTO budgetDTO) {
        log.info("Creating new budget for user: {}", budgetDTO.getUserId());
        Budget budget = Budget.builder()
                .userId(budgetDTO.getUserId())
                .amount(budgetDTO.getAmount())
                .month(budgetDTO.getMonth())
                .build();
        return convertToDTO(budgetRepository.save(budget));
    }

    /**
     * @brief 특정 월의 예산 조회
     * @param userId 사용자 ID
     * @return 해당 월의 예산 정보 목록
     */
    @Override
    public List<BudgetDTO> getMonthlyBudget(String userId) {
        List<Budget> budgets = budgetRepository.findByUserId(userId);
        return budgets.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * @brief Budget 엔티티를 DTO로 변환
     * @param budget 변환할 Budget 엔티티
     * @return 변환된 BudgetDTO
     */
    private BudgetDTO convertToDTO(Budget budget) {
        return BudgetDTO.builder()
                .budgetId(budget.getId())
                .userId(budget.getUserId())
                .amount(budget.getAmount())
                .month(budget.getMonth())
                .build();
    }
} 