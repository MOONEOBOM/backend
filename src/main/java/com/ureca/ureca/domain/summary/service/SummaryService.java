package com.ureca.ureca.domain.summary.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ureca.ureca.domain.gemini.service.GeminiService;
import com.ureca.ureca.domain.summary.dto.HighlightItemDto;
import com.ureca.ureca.domain.summary.dto.SummaryDetailResponseDto;
import com.ureca.ureca.domain.summary.dto.SummaryItem;
import com.ureca.ureca.domain.summary.dto.SummaryListResponseDto;
import com.ureca.ureca.domain.summary.dto.SummaryRequestDto;
import com.ureca.ureca.domain.summary.dto.SummaryResponseDto;
import com.ureca.ureca.domain.summary.mapper.SummaryMapper;
import com.ureca.ureca.global.common.exception.BusinessException;
import com.ureca.ureca.global.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SummaryService {

  private final SummaryMapper summaryMapper;
  private final GeminiService geminiService;

  /**
   * 상담내역 리스트 조회 view=all: 무한스크롤 (cursor + limit) view=recent: 최근 2개 (cursor 무시)
   */
  public SummaryListResponseDto getSummaries(Long userId, Integer limit, Long cursor, String view) {
    String safeView = (view == null) ? "all" : view;

    // 기본 limit 정책
    int pageSize;
    if (safeView.equals("recent")) {
      pageSize = 2;
    } else {
      pageSize = (limit != null) ? limit : 10;
    }

    List<SummaryItem> items;

    if (safeView.equals("recent")) {
      items = summaryMapper.selectRecentSummaries(userId, pageSize);

      return SummaryListResponseDto.builder().items(items).nextCursor(null).build();
    }

    // view=all (무한스크롤)
    items = summaryMapper.selectSummaries(userId, pageSize, cursor);

    Long nextCursor = null;
    if (items != null && items.size() == pageSize) {
      // 마지막 아이템의 id를 다음 cursor로 내려줌
      nextCursor = items.get(items.size() - 1).getId();
    }

    return SummaryListResponseDto.builder().items(items).nextCursor(nextCursor).build();
  }

  /**
   * 상담 요약 자세히 보기 includeHighlights=true 시 핵심 대화 포함
   */
  @Transactional(readOnly = true)
  public SummaryDetailResponseDto getSummaryDetail(Long userId, Long id,
      boolean includeHighlights) {
    SummaryDetailResponseDto dto = summaryMapper.selectSummaryDetail(userId, id);
    if (dto == null) {
      throw new BusinessException(ErrorCode.SUMMARY_NOT_FOUND);
    }

    if (includeHighlights) {
      List<HighlightItemDto> highlights = summaryMapper.selectHighlights(dto.getId());
      dto.setHighlights(highlights);
    } else {
      dto.setHighlights(List.of()); // 프론트 처리 편하게 빈 배열
    }

    return dto;
  }



  /**
   * 상담 내용 분석...요약하려면
   * 
   * @param requestDto
   * @return
   */
  public SummaryResponseDto createSummary(SummaryRequestDto requestDto) {

    // 데이터 있나없나 확인
    if (requestDto == null) {
      throw new BusinessException(ErrorCode.NULL_REFERENCE_ERROR);
    }

    // 필수 내용들이 있는지 확인 (누락 여부 확인)
    if (requestDto.getMessages() == null || requestDto.getMessages().isEmpty()) {
      throw new BusinessException(ErrorCode.REQUIRED_FIELD_MISSING);
    }
    for (SummaryRequestDto.ChatMessage msg : requestDto.getMessages()) {
      if (msg == null || msg.getSpeaker() == null || msg.getSpeaker().isBlank()
          || msg.getMessage() == null || msg.getMessage().isBlank()) {
        throw new BusinessException(ErrorCode.REQUIRED_FIELD_MISSING);
      }
    }

    // Gemini 불러서 상담 내용 요약 시작
    try {
      Object summaryconversation = requestDto.getMessages();

      log.info("[SummaryService] 요약 생성 요청 시작 (분야 제외)");
      return geminiService.summaryCreate(summaryconversation);
    }

    catch (BusinessException e) {
      throw e;
    } catch (Exception e) {
      // 에러
      log.error("[SummaryService Error] : {}", e.getMessage());
      throw new BusinessException(ErrorCode.INTERNAL_ERROR);
    }
  }
}


