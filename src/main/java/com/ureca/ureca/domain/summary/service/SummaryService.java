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
    	
    	if (requestDto == null) {
    		throw new BusinessException(ErrorCode.NULL_REFERENCE_ERROR);
	    }
        
    	// 필수 내용들이 있는지 확인 (누락 여부 확인)
        if (requestDto.getCategory_label() == null || requestDto.getCategory_label().isBlank() ||
            requestDto.getStt_text() == null || requestDto.getStt_text().isBlank() ||
            requestDto.getKeywords() == null || requestDto.getKeywords().isEmpty()) {
          
        	throw new BusinessException(ErrorCode.REQUIRED_FIELD_MISSING);
        }
        
        try {
            // GeminiService 호출 -> 요약 시작
            String category = requestDto.getCategory_label();
            String sttText = requestDto.getStt_text();
            List<String> keywords = requestDto.getKeywords();
            
            log.info("[SummaryService] 요약 생성 요청 - 분야: {}", category);
            
            return geminiService.summaryCreate(category, sttText, keywords);
        }
        
	    catch (BusinessException e) {
	        throw e;
	    }
	    catch (Exception e) {
	        // 에러
	        log.error("[Summary Error] 예상치 못한 오류 발생: {}", e.getMessage(), e);
	        throw new BusinessException(ErrorCode.INTERNAL_ERROR);
	    }
    }
}