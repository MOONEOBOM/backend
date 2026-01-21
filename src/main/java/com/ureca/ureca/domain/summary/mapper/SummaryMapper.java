package com.ureca.ureca.domain.summary.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.ureca.ureca.domain.summary.dto.HighlightItemDto;
import com.ureca.ureca.domain.summary.dto.SummaryDetailResponseDto;
import com.ureca.ureca.domain.summary.dto.SummaryItem;

@Mapper
public interface SummaryMapper {

  // 무한스크롤: cursor 없으면 첫 페이지, 있으면 id < cursor 다음 페이지
  List<SummaryItem> selectSummaries(@Param("userId") Long userId, @Param("limit") Integer limit,
      @Param("cursor") Long cursor);

  // 최근 조회
  List<SummaryItem> selectRecentSummaries(@Param("userId") Long userId, @Param("limit") Integer limit);

  // 상담내용 하나보기
  SummaryDetailResponseDto selectSummaryDetail(@Param("userId") Long userId, @Param("id") Long id);

  // 처음 요약된페이지일떄 하이라이트버블 체크
  List<HighlightItemDto> selectHighlights(@Param("summaryId") Long summaryId);
}

