package com.ureca.ureca.domain.chatbot.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ureca.ureca.domain.chatbot.dto.ChatbotRequestDto;
import com.ureca.ureca.domain.chatbot.dto.ChatbotResponseDto;
import com.ureca.ureca.domain.chatbot.service.ChatbotService;
import com.ureca.ureca.global.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
@Slf4j
public class ChatbotController {
  private final ChatbotService chatbotService;

  @PostMapping("/answer")
  public ApiResponse<ChatbotResponseDto> createChatAnswer(@Valid @RequestBody ChatbotRequestDto requestDto) {
	  ChatbotResponseDto response= chatbotService.createChatAnswer(requestDto);
	  return ApiResponse.ok("챗봇 답변 성공", response);
  }
}
