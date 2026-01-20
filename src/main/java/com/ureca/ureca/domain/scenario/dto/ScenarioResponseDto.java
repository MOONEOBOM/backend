package com.ureca.ureca.domain.scenario.dto;


import java.util.List;

import lombok.Data;

@Data
public class ScenarioResponseDto {
	private List<ScenarioDetail> scenario;
	private List<String> keywords;
	
	@Data
    public static class ScenarioDetail {
        private String role;    // "agent", "user"
        private String message; // 실제 대화 내용
    }
}
