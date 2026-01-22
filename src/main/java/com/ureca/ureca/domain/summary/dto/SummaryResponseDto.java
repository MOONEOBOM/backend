package com.ureca.ureca.domain.summary.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class SummaryResponseDto {
	
    private String title;  // 요약본 제목 (카드 타이틀)
    private String summary;  // 상담 요약본 (3 ~ 4줄)
    @JsonProperty("core_chat")
    private CoreChat corechat;  // 핵심 채팅

    @Data
    public static class CoreChat {
        // 상담사 핵심 정보
        private String counselor;        // "상담사"
        private String counselormessage; // 상담사 핵심 대화 내용
        
        // 고객 핵심 정보
        private String customer;         // "고객"
        private String customermessage;  // 고객 핵심 대화 내용
    }
}