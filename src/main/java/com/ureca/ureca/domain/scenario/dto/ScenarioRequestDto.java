package com.ureca.ureca.domain.scenario.dto;


import java.util.List;

import lombok.Data;

@Data
public class ScenarioRequestDto {
	private String categoryKey;
	private List<String> reasonKey;
}
