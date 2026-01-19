package com.ureca.ureca.domain.scenario.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SttRequestDto {
	@NotBlank(message = "dataKey는 필수입니다.")
	private String dataKey;
}
