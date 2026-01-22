package com.ureca.ureca.domain.summary.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class SummaryResponseDto {
	
    private String title;  // 요약본 제목 (카드 타이틀)
    private String summary;  // 상담 요약본 (3 ~ 4줄)
    @JsonProperty("core_chat")
    private List<CoreChat> corechat;  // 핵심 채팅

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CoreChat {
    	private String speaker;
        private String message;
    }
}