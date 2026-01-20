package com.ureca.ureca.domain.gemini.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import com.ureca.ureca.domain.gemini.dto.request.GeminiRequestDto;
import com.ureca.ureca.domain.gemini.dto.response.GeminiResponseDto;
import com.ureca.ureca.domain.scenario.dto.ScenarioResponseDto;
import com.ureca.ureca.global.api.gemini.GeminiInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static com.ureca.ureca.global.constant.GeminiConstant.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeminiService {

    private final GeminiInterface geminiInterface;
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    
    public ScenarioResponseDto scenarioCreate(String categoryLabel, String reasonLabel) {
    	try {
    	// 프롬프트 작성
    	String prompt = String.format(SCENARIO_CREATE_PROMPT_TEMPLATE, categoryLabel, reasonLabel);
    	log.info("Gemini 요청 : {},사유: {}", categoryLabel,reasonLabel);
    	// Gemini 호출
        GeminiRequestDto geminiRequest = new GeminiRequestDto(prompt);
        GeminiResponseDto response = getCompletion(geminiRequest);
        
        // Gemini 응답 텍스트(JSON 문자열) 추출
        String jsonResponse = response
                .getCandidates()
                .stream()
                .findFirst()
                .flatMap(candidate -> candidate.getContent().getParts()
                        .stream()
                        .findFirst()
                        .map(GeminiResponseDto.TextPart::getText))
                .orElse(null);
        
        jsonResponse = jsonResponse
                .replaceAll("(?i)```json", "")
                .replaceAll("```", "")
                .trim();

        log.info("Gemini 응답 정제 후 JSON: {}", jsonResponse);
        
        return objectMapper.readValue(jsonResponse, ScenarioResponseDto.class);
        
    	}catch(Exception e) {
    		log.error("Gemini 서비스 처리 중 에러 발생: {}", e.getMessage());
    		throw new RuntimeException("Gemini API 호출 실패", e);
    	}
    }

    private GeminiResponseDto getCompletion(GeminiRequestDto request) {
        return geminiInterface.getCompletion(GEMINI_MODEL, request);
    }

}
