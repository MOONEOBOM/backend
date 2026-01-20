package com.ureca.ureca.domain.scenario.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ureca.ureca.domain.gemini.service.GeminiService;
import com.ureca.ureca.domain.scenario.dto.ClovaResponse;
import com.ureca.ureca.domain.scenario.dto.ScenarioRequestDto;
import com.ureca.ureca.domain.scenario.dto.ScenarioResponseDto;
import com.ureca.ureca.domain.scenario.dto.SttResultDto;
import com.ureca.ureca.global.common.exception.BusinessException;
import com.ureca.ureca.global.common.exception.ErrorCode;
import com.ureca.ureca.global.external.clova.ClovaSpeechClient;
import com.ureca.ureca.global.external.clova.ClovaSpeechClient.NestRequestEntity;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScenarioService {

  private final ClovaSpeechClient clovaSpeechClient;
  private final ObjectMapper objectMapper;
  private final GeminiService geminiService;

  // public String transcribeByUpload(File file) {
  // NestRequestEntity request = new NestRequestEntity();
  // // 필요하면 request.setDiarization(...), request.setBoostings(...), request.setSed(...) 세팅
  // return clovaSpeechClient.upload(file, request);
  // }

  public String transcribeByUrl(String url) {
    NestRequestEntity request = new NestRequestEntity();
    return clovaSpeechClient.url(url, request);
  }

  public SttResultDto transcribeByObjectStorage(String dataKey) {
    if (dataKey == null || dataKey.isBlank()) {
      throw new BusinessException(ErrorCode.SCENARIO_INVALID_DATA_KEY);
    }

    // 1) 클로바 호출 > raw JSON 문자열
    ClovaSpeechClient.NestRequestEntity req = new ClovaSpeechClient.NestRequestEntity();
    String rawJson = clovaSpeechClient.objectStorage(dataKey, req);

    // 2) raw JSON(String) > ClovaSttResponse DTO
    ClovaResponse clova = parseClovaResponse(rawJson);

    // 3) segments → (speaker, text, startMs, endMs, confidence) 변환 (순서 유지)
    List<ClovaResponse.Segment> segments = clova.getSegments();
    if (segments == null || segments.isEmpty()) {
      throw new BusinessException(ErrorCode.SEGMENTS_NOT_FOUND);
    }

    List<SttResultDto.SpeakerLineDto> lines = segments.stream().map(seg -> {
      SttResultDto.SpeakerLineDto line = new SttResultDto.SpeakerLineDto();

      String speaker = (seg.getSpeaker() != null && seg.getSpeaker().getName() != null)
          ? seg.getSpeaker().getName()
          : "UNKNOWN";

      line.setSpeaker(speaker);
      line.setText(seg.getText());
      line.setStartMs(seg.getStart());
      line.setEndMs(seg.getEnd());
      line.setConfidence(seg.getConfidence());

      return line;
    }).toList();

    // 4) 최종 응답 DTO 조립 (전체 텍스트 + 전체 confidence + 라인별)
    SttResultDto result = new SttResultDto();
    result.setFullText(clova.getText());
    result.setConfidence(clova.getConfidence());
    result.setLines(lines);

    return result;
  }

  private ClovaResponse parseClovaResponse(String rawJson) {
    try {
      return objectMapper.readValue(rawJson, ClovaResponse.class);
    } catch (Exception e) {
      throw new BusinessException(ErrorCode.CLOVA_RESPONSE_PARSE_FAILED);
    }
  }
  public ScenarioResponseDto createScenario(ScenarioRequestDto requestDto) {
	
		  if(requestDto == null) {
		        throw new BusinessException(ErrorCode.NULL_REFERENCE_ERROR);
		    }
		  if(requestDto.getCategoryKey()==null || requestDto.getReasonKey()==null) {
			  throw new BusinessException(ErrorCode.NULL_REFERENCE_ERROR);
		  }
		  if (requestDto.getCategoryKey().isBlank() || requestDto.getReasonKey().isEmpty()) {
		        throw new BusinessException(ErrorCode.REQUIRED_FIELD_MISSING);
		    }
	  try {
		  String category = requestDto.getCategoryKey();
		  String combinedReasons = String.join(", ", requestDto.getReasonKey());
		  return geminiService.scenarioCreate(category, combinedReasons);
	  }
	  catch(Exception e) {
		  throw new BusinessException(ErrorCode.INTERNAL_ERROR);
	  }
	  
	  
	  
 
  }
}
