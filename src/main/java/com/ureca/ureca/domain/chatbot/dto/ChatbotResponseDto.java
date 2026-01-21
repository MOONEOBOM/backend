package com.ureca.ureca.domain.chatbot.dto;


import java.util.List;

import lombok.Data;

@Data
public class ChatbotResponseDto {
	private List<ScenarioDetail> answer;
	
	@Data
    public static class ScenarioDetail {
        private String normal;    // 일반적인 답변
        private String easy; // 쉬운 답변
    }
}
