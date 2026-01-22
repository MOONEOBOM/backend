package com.ureca.ureca.domain.summary.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CounselSummaryHighlight {
  private Long summaryId;
  private Integer seq;
  private String speaker; // agent | user
  private String text;
}
