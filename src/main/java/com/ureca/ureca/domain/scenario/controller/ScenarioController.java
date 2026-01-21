package com.ureca.ureca.domain.scenario.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ureca.ureca.domain.gemini.service.GeminiService;
import com.ureca.ureca.domain.scenario.dto.ScenarioRequestDto;
import com.ureca.ureca.domain.scenario.dto.ScenarioResponseDto;
import com.ureca.ureca.domain.scenario.dto.SttRequestDto;
import com.ureca.ureca.domain.scenario.dto.SttResultDto;
import com.ureca.ureca.domain.scenario.service.ScenarioService;
import com.ureca.ureca.global.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/scenario")
@Slf4j
public class ScenarioController {
  private final ScenarioService scenarioService;

  @PostMapping("/stt")
  public ApiResponse<SttResultDto> stt(@Valid @RequestBody SttRequestDto request) {
    SttResultDto result = scenarioService.transcribeByObjectStorage(request.getDataKey());
    return ApiResponse.ok("통화기록 변환 성공", result);
  }
  @PostMapping("/create")
  public ApiResponse<ScenarioResponseDto> createScenario(@Valid @RequestBody ScenarioRequestDto requestDto) {
	  ScenarioResponseDto response= scenarioService.createScenario(requestDto);
	  return ApiResponse.ok("시나리오 생성 성공", response);
  }
}
