package com.ureca.ureca.domain.summary.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class SummaryItem {
  private Long id;
  private String title;
  private LocalDate createdDate; // "2026-01-21"

}
