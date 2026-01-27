package com.ureca.ureca.domain.call.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ureca.ureca.domain.call.dto.CallCounselListResponseDto;
import com.ureca.ureca.domain.call.dto.CallMessagesResponseDto;
import com.ureca.ureca.domain.call.service.CallService;
import com.ureca.ureca.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/call")
@Slf4j
public class CallController {
  private final CallService callService;

  @GetMapping
  public ApiResponse<List<CallCounselListResponseDto>> getCallCounselList() {
    List<CallCounselListResponseDto> response = callService.getCallCounselList();
    return ApiResponse.ok("통화목록 조회 완료", response);
  }

  @GetMapping("/{callId}")
  public ApiResponse<List<CallMessagesResponseDto>> getCallMessages(
      @PathVariable("callId") Long callId) {

    List<CallMessagesResponseDto> response = callService.getMessages(callId);
    return ApiResponse.ok("통화상담 메시지 조회 완료", response);
  }
}
