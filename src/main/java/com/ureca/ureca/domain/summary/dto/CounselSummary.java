package com.ureca.ureca.domain.summary.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CounselSummary {
  private Long id;
  private Long userId;
  private String title;
  private String content;
}
