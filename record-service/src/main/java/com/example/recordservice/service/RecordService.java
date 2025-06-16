package com.example.recordservice.service;

import com.example.recordservice.dto.RecordDTO;

import java.time.LocalDate;
import java.util.List;

/**
 * @brief 가계부 기록을 관리하는 서비스 인터페이스
 * @details 사용자의 수입/지출 기록을 생성, 조회, 수정, 삭제하는 기능을 제공합니다.
 */
public interface RecordService {
    /**
     * @brief 월별 가계부 기록 조회
     * @param userId 사용자 ID
     * @param month 조회할 월
     * @return 해당 월의 가계부 기록 목록
     */
    List<RecordDTO> getMonthlyRecords(String userId, LocalDate month);

    /**
     * @brief 가계부 기록 생성
     * @param recordDTO 생성할 가계부 기록 정보
     * @return 생성된 가계부 기록
     */
    RecordDTO createRecord(RecordDTO recordDTO);

    /**
     * @brief 가계부 기록 조회
     * @param recordId 조회할 기록 ID
     * @return 조회된 가계부 기록
     */
    RecordDTO getRecord(Long recordId);

    /**
     * @brief 가계부 기록 삭제
     * @param recordId 삭제할 기록 ID
     */
    void deleteRecord(Long recordId);

    /**
     * @brief 가계부 기록 수정
     * @param recordId 수정할 기록 ID
     * @param recordDTO 수정할 가계부 기록 정보
     * @return 수정된 가계부 기록
     */
    RecordDTO updateRecord(Long recordId, RecordDTO recordDTO);
} 