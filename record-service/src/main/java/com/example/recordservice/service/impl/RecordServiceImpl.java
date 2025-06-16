package com.example.recordservice.service.impl;

import com.example.recordservice.dto.RecordDTO;
import com.example.recordservice.entity.Record;
import com.example.recordservice.repository.RecordRepository;
import com.example.recordservice.service.RecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @brief 가계부 기록 관리 서비스 구현체
 * @details RecordService 인터페이스의 구현체로, 가계부 기록의 CRUD 기능을 제공합니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecordServiceImpl implements RecordService {
    private final RecordRepository recordRepository;

    /**
     * @brief 특정 월의 가계부 기록 목록을 조회
     * @param userId 사용자 ID
     * @param month 조회할 월
     * @return 해당 월의 가계부 기록 목록
     */
    @Override
    public List<RecordDTO> getMonthlyRecords(String userId, LocalDate month) {
        log.info("Fetching monthly records for user: {} and month: {}", userId, month);
        LocalDate startDate = month.withDayOfMonth(1);
        LocalDate endDate = month.withDayOfMonth(month.lengthOfMonth());
        
        List<Record> records = recordRepository.findByUserIdAndDateBetween(userId, startDate, endDate);
        log.debug("Found {} records", records.size());
        
        return records.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * @brief 새로운 가계부 기록 생성
     * @param recordDTO 생성할 가계부 기록 정보
     * @return 생성된 가계부 기록
     */
    @Override
    @Transactional
    public RecordDTO createRecord(RecordDTO recordDTO) {
        log.info("Creating new record for user: {}", recordDTO.getUserId());
        Record record = convertToEntity(recordDTO);
        Record savedRecord = recordRepository.save(record);
        log.debug("Created record with ID: {}", savedRecord.getRecId());
        return convertToDTO(savedRecord);
    }

    /**
     * @brief 특정 가계부 기록 조회
     * @param recordId 조회할 가계부 기록 ID
     * @return 조회된 가계부 기록
     */
    @Override
    public RecordDTO getRecord(Long recordId) {
        log.info("Fetching record with ID: {}", recordId);
        Record record = recordRepository.findById(recordId)
                .orElseThrow(() -> {
                    log.error("Record not found with ID: {}", recordId);
                    return new RuntimeException("Record not found");
                });
        return convertToDTO(record);
    }

    /**
     * @brief 가계부 기록 삭제
     * @param recordId 삭제할 가계부 기록 ID
     */
    @Override
    @Transactional
    public void deleteRecord(Long recordId) {
        log.info("Deleting record with ID: {}", recordId);
        if (!recordRepository.existsById(recordId)) {
            log.error("Record not found with ID: {}", recordId);
            throw new RuntimeException("Record not found");
        }
        recordRepository.deleteById(recordId);
        log.debug("Successfully deleted record with ID: {}", recordId);
    }

    /**
     * @brief 가계부 기록 수정
     * @param recordId 수정할 가계부 기록 ID
     * @param recordDTO 수정할 가계부 기록 정보
     * @return 수정된 가계부 기록
     */
    @Override
    @Transactional
    public RecordDTO updateRecord(Long recordId, RecordDTO recordDTO) {
        log.info("Updating record with ID: {}", recordId);
        Record existingRecord = recordRepository.findById(recordId)
                .orElseThrow(() -> {
                    log.error("Record not found with ID: {}", recordId);
                    return new RuntimeException("Record not found");
                });

        Record updatedRecord = Record.builder()
                .recId(existingRecord.getRecId())
                .userId(existingRecord.getUserId())
                .amount(recordDTO.getAmount())
                .date(recordDTO.getDate())
                .category(recordDTO.getCategory())
                .type(recordDTO.getType())
                .memo(recordDTO.getMemo())
                .build();

        Record savedRecord = recordRepository.save(updatedRecord);
        log.debug("Successfully updated record with ID: {}", recordId);
        return convertToDTO(savedRecord);
    }

    /**
     * @brief Record 엔티티를 DTO로 변환
     * @param record 변환할 Record 엔티티
     * @return 변환된 RecordDTO
     */
    private RecordDTO convertToDTO(Record record) {
        return RecordDTO.builder()
                .recId(record.getRecId())
                .userId(record.getUserId())
                .amount(record.getAmount())
                .date(record.getDate())
                .category(record.getCategory())
                .type(record.getType())
                .memo(record.getMemo())
                .build();
    }

    /**
     * @brief DTO를 Record 엔티티로 변환
     * @param dto 변환할 RecordDTO
     * @return 변환된 Record 엔티티
     */
    private Record convertToEntity(RecordDTO dto) {
        return Record.builder()
                .userId(dto.getUserId())
                .amount(dto.getAmount())
                .date(dto.getDate())
                .category(dto.getCategory())
                .type(dto.getType())
                .memo(dto.getMemo())
                .build();
    }
} 