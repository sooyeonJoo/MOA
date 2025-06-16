package com.example.recordservice.controller;

import com.example.recordservice.dto.RecordDTO;
import com.example.recordservice.service.RecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * @brief 가계부 기록을 관리하는 컨트롤러
 * @details 사용자의 수입/지출 기록을 생성, 조회, 수정, 삭제하는 API를 제공합니다.
 */
@CrossOrigin(
    origins = "http://localhost:8080, http://localhost:8081",
    allowCredentials = "true",
    allowedHeaders = "*",
    methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH,
                RequestMethod.DELETE, RequestMethod.OPTIONS }
)
@RestController
@RequiredArgsConstructor
@Tag(name = "record", description = "Access to Petstore orders")
public class RecordController {
    private final RecordService recordService;

    @GetMapping("/records")
    @Operation(summary = "지출/수입 모든 내역 조회 API", description = "회원의 ID를 기준으로 정보를 조회한다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK - 요청 처리 성공"),
        @ApiResponse(responseCode = "500", description = "Unexpected error")
    })
    public ResponseEntity<List<RecordDTO>> getRecords(
            @Parameter(description = "조회할 월 (ex.2025-07)") 
            @RequestParam(value = "month", required = false) LocalDate month,
            @Parameter(description = "사용자 ID") 
            @RequestParam(value = "userId", required = true) String userId) {
        System.out.println("▶▶▶ GET /records month: " + month + ", userId: " + userId);
        List<RecordDTO> list = recordService.getMonthlyRecords(userId, month);
        System.out.println("▶▶▶ Returned " + list.size() + " records");
        return ResponseEntity.ok(list);
    }

    @PostMapping("/record")
    @Operation(summary = "수입/지출 등록 API", description = "회원의 ID기준으로 정보를 등록한다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK - 요청 처리 성공"),
        @ApiResponse(responseCode = "500", description = "Unexpected error")
    })
    public ResponseEntity<RecordDTO> createRecord(
            @Parameter(description = "등록할 수입/지출 내역") @RequestBody RecordDTO recordDTO) {
        System.out.println("▶▶▶ POST /record payload: " + recordDTO);
        RecordDTO saved = recordService.createRecord(recordDTO);
        System.out.println("▶▶▶ Saved record: " + saved);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/record/{recordID}")
    @Operation(summary = "수입/지출 상세내역 조회 API", description = "회원의 ID와 날짜 기준으로 정보를 조회한다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK - 요청 처리 성공"),
        @ApiResponse(responseCode = "500", description = "Unexpected error")
    })
    public ResponseEntity<RecordDTO> getRecord(
            @Parameter(description = "레코드 번호") @PathVariable("recordID") Long recordId) {
        return ResponseEntity.ok(recordService.getRecord(recordId));
    }

    @DeleteMapping("/record/{recordID}")
    @Operation(summary = "수입/지출 내역 삭제 API", description = "회원의 ID와 날짜 기준으로 정보를 삭제한다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK - 요청 처리 성공"),
        @ApiResponse(responseCode = "500", description = "Unexpected error")
    })
    public ResponseEntity<Void> deleteRecord(
            @Parameter(description = "레코드 번호") @PathVariable("recordID") Long recordId) {
        recordService.deleteRecord(recordId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/record/{recordID}")
    @Operation(summary = "수입/지출 상세내역 수정 API", description = "회원의 ID와 날짜 기준으로 정보를 수정한다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK - 요청 처리 성공"),
        @ApiResponse(responseCode = "500", description = "Unexpected error")
    })
    public ResponseEntity<RecordDTO> updateRecord(
            @Parameter(description = "레코드 번호") @PathVariable("recordID") Long recordId,
            @Parameter(description = "수정할 수입/지출 내역") @RequestBody RecordDTO recordDTO) {
        return ResponseEntity.ok(recordService.updateRecord(recordId, recordDTO));
    }
} 