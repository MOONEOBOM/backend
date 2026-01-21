package com.ureca.ureca.domain.summary.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SummaryDetailResponseDto {
  private Long id;
  private String title;
  private String content;
  private String createdDate; // "YYYY-MM-DD"

  // includeHighlights=false면 빈 배열
  private List<HighlightItemDto> highlights;


}
