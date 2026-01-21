package com.ureca.ureca.domain.summary.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ureca.ureca.domain.summary.dto.HighlightItemDto;
import com.ureca.ureca.domain.summary.dto.SummaryDetailResponseDto;
import com.ureca.ureca.domain.summary.dto.SummaryItem;
import com.ureca.ureca.domain.summary.dto.SummaryListResponseDto;
import com.ureca.ureca.domain.summary.mapper.SummaryMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SummaryService {

  private final SummaryMapper summaryMapper;

  /**
   * 상담내역 리스트 조회 view=all: 무한스크롤 (cursor + limit) view=recent: 최근 2개 (cursor 무시)
   */
  public SummaryListResponseDto getSummaries(Long userId, Integer limit, Long cursor, String view) {
    String safeView = (view == null) ? "all" : view;

    // 기본 limit 정책
    int pageSize = (limit != null) ? limit : (safeView.equals("recent") ? 2 : 10);

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

  @Transactional(readOnly = true)
  public SummaryDetailResponseDto getSummaryDetail(Long userId, Long id,
      boolean includeHighlights) {
    SummaryDetailResponseDto dto = summaryMapper.selectSummaryDetail(userId, id);
    if (dto == null) {
      // 너희 프로젝트 예외 포맷 있으면 그걸로 바꿔도 됨
      throw new IllegalArgumentException("Summary not found");
    }

    if (includeHighlights) {
      List<HighlightItemDto> highlights = summaryMapper.selectHighlights(dto.getId());
      dto.setHighlights(highlights);
    } else {
      dto.setHighlights(List.of()); // 프론트 처리 편하게 빈 배열
    }

    return dto;
  }
}

