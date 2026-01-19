package com.ureca.ureca.domain.scenario.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ureca.ureca.domain.scenario.dto.SttRequestDto;
import com.ureca.ureca.domain.scenario.dto.SttResultDto;
import com.ureca.ureca.domain.scenario.service.ScenarioService;
import com.ureca.ureca.global.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/scenario")
public class ScenarioController {
  private final ScenarioService scenarioService;

  @PostMapping("/stt")
  public ApiResponse<SttResultDto> stt(@Valid @RequestBody SttRequestDto request) {
    SttResultDto result = scenarioService.transcribeByObjectStorage(request.getDataKey());
    return ApiResponse.ok("통화기록 변환 성공", result);
  }
}
