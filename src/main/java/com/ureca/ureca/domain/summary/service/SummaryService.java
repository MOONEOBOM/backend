package com.ureca.ureca.domain.summary.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.ureca.ureca.domain.gemini.service.GeminiService;
import com.ureca.ureca.domain.summary.dto.SummaryRequestDto;
import com.ureca.ureca.domain.summary.dto.SummaryResponseDto;
import com.ureca.ureca.global.common.exception.BusinessException;
import com.ureca.ureca.global.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SummaryService {

    private final GeminiService geminiService;

    /**
     * 상담 내용 분석...요약하려면
     * @param requestDto
     * @return
     */
    public SummaryResponseDto createSummary(SummaryRequestDto requestDto) {
    	
    	// 데이터 있나없나 확인
    	if (requestDto == null) {
    		throw new BusinessException(ErrorCode.NULL_REFERENCE_ERROR);
	    }
        
    	// 필수 내용들이 있는지 확인 (누락 여부 확인)
    	if (requestDto.getMessages() == null || requestDto.getMessages().isEmpty()) {
            throw new BusinessException(ErrorCode.REQUIRED_FIELD_MISSING);
        }
    	for (SummaryRequestDto.ChatMessage msg : requestDto.getMessages()) {
    		if (msg == null || msg.getSpeaker() == null || msg.getSpeaker().isBlank()
    				|| msg.getMessage() == null || msg.getMessage().isBlank()) {
    			throw new BusinessException(ErrorCode.REQUIRED_FIELD_MISSING);
    		}
    	}
    	
    	// Gemini 불러서 상담 내용 요약 시작
    	try {
    		Object summaryconversation = requestDto.getMessages();
            
            log.info("[SummaryService] 요약 생성 요청 시작 (분야 제외)");
            return geminiService.summaryCreate("", summaryconversation);
        }
        
	    catch (BusinessException e) {
	        throw e;
	    }
	    catch (Exception e) {
	        // 에러
	    	log.error("[SummaryService Error] : {}", e.getMessage());
	        throw new BusinessException(ErrorCode.INTERNAL_ERROR);
	    }
    }
}