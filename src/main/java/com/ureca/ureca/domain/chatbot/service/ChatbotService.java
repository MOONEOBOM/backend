package com.ureca.ureca.domain.chatbot.service;


import org.springframework.stereotype.Service;
import com.ureca.ureca.domain.chatbot.dto.ChatbotRequestDto;
import com.ureca.ureca.domain.chatbot.dto.ChatbotResponseDto;
import com.ureca.ureca.domain.gemini.service.GeminiService;
import com.ureca.ureca.global.common.exception.BusinessException;
import com.ureca.ureca.global.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatbotService {

  private final GeminiService geminiService;
  
  public ChatbotResponseDto createChatAnswer(ChatbotRequestDto requestDto) {
	
		  if(requestDto == null) {
		        throw new BusinessException(ErrorCode.NULL_REFERENCE_ERROR);
		    }
		  if(requestDto.getChat()==null) {
			  throw new BusinessException(ErrorCode.NULL_REFERENCE_ERROR);
		  }
		  if (requestDto.getChat().isBlank()) {
		        throw new BusinessException(ErrorCode.REQUIRED_FIELD_MISSING);
		    }
	  try {
		  String chat = requestDto.getChat();
		  return geminiService.chatCreateAnswer(chat);
	  }
	  catch (BusinessException e) {
	  	  throw e;
	    } catch (Exception e) {
	  	  throw new BusinessException(ErrorCode.INTERNAL_ERROR);
	    }
	  
	  
	  
 
  }
}
