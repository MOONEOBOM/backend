package com.ureca.ureca.domain.summary.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ureca.ureca.domain.scenario.controller.ScenarioController;
import com.ureca.ureca.domain.scenario.service.ScenarioService;
import com.ureca.ureca.domain.summary.dto.SummaryRequestDto;
import com.ureca.ureca.domain.summary.dto.SummaryResponseDto;
import com.ureca.ureca.domain.summary.service.SummaryService;
import com.ureca.ureca.global.common.response.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/summary")
@Slf4j
public class SummaryController{
	
	private final SummaryService summaryService;
	
	@PostMapping("/summarize") 
    public ApiResponse<SummaryResponseDto> generateSummary(@Valid @RequestBody SummaryRequestDto requestDto) {
        log.info("상담 요약 생성 요청 시작");
        
        SummaryResponseDto response = summaryService.createSummary(requestDto);
        
        return ApiResponse.ok("상담 요약 생성 성공", response);
    }
}