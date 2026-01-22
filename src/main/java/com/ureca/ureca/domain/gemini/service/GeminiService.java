package com.ureca.ureca.domain.gemini.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ureca.ureca.domain.chatbot.dto.ChatbotResponseDto;
import com.ureca.ureca.domain.gemini.dto.request.GeminiRequestDto;
import com.ureca.ureca.domain.gemini.dto.response.GeminiResponseDto;
import com.ureca.ureca.domain.scenario.dto.ScenarioResponseDto;
import com.ureca.ureca.domain.summary.dto.SummaryResponseDto;
import com.ureca.ureca.global.api.gemini.GeminiInterface;
import com.ureca.ureca.global.common.exception.BusinessException;
import com.ureca.ureca.global.common.exception.ErrorCode;

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
    	String prompt = String.format(SCENARIO_CREATE_PROMPT_TEMPLATE, categoryLabel, reasonLabel);
    	log.info("Gemini 요청 : {},사유: {}", categoryLabel,reasonLabel);
    	return executeGeminiRequest(prompt, ScenarioResponseDto.class);
    }
    
    public ChatbotResponseDto chatCreateAnswer(String chat) {
    	String prompt = String.format(CHATBOT_PROMPT_TEMPLATE, chat);
        log.info("Gemini 챗봇 질문: {}", chat);
        
        return executeGeminiRequest(prompt, ChatbotResponseDto.class);
    }
    
    // 상담 요약 생성 (summary)
    public SummaryResponseDto summaryCreate(Object conversationData) {
    	
    	try {
            // 1) 입력받은 JSON 객체를 문자열로 변환 (Gemini 프롬프트에 넣는 용도)
    		String conversationJson = objectMapper.writeValueAsString(conversationData);

            // 2) 프롬프트 템플릿 치환
    		String prompt = SCENARIO_SUMMARY_PROMPT_TEMPLATE
    		        .replace("{{conversation}}", conversationJson);
            
    		log.info("Gemini 상담 요약 요청");
            
            return executeGeminiRequest(prompt, SummaryResponseDto.class);
            
        } catch (JsonProcessingException e) {
            log.error("[Gemini Error] 입력 데이터 직렬화 실패: {}", e.getMessage());
            throw new BusinessException(ErrorCode.INVALID_REQUEST);
        }
    }
    
    private <T> T executeGeminiRequest(String prompt, Class<T> responseType) {
        try {
            // 1. Gemini 호출
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
                  .orElse("");
          if(jsonResponse.isBlank()) {
          	throw new BusinessException(ErrorCode.GEMINI_EMPTY_RESPONSE);
          }
        jsonResponse = jsonResponse
                .replaceAll("(?i)```json", "")
                .replaceAll("```", "")
                .trim();

        log.info("Gemini 응답 정제 후 JSON: {}", jsonResponse);
        try {
          return objectMapper.readValue(jsonResponse, responseType);
        } 
        catch (JsonProcessingException e) {
            throw new BusinessException(ErrorCode.GEMINI_RESPONSE_PARSE_FAILED);
        }
    }
        catch (BusinessException e) {
            throw e;
        }
        catch (Exception e) {
            log.error("[Gemini Error] 예상치 못한 오류 발생: {}", e.getMessage(), e);
            throw new BusinessException(ErrorCode.INTERNAL_ERROR);
        }
    }
    
    private GeminiResponseDto getCompletion(GeminiRequestDto request) {
        return geminiInterface.getCompletion(GEMINI_MODEL, request);
    }

}
