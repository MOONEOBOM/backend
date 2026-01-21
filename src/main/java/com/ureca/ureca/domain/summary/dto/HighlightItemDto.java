package com.ureca.ureca.domain.summary.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HighlightItemDto {
  private Integer seq;
  private String speaker; // "mooneo" | "user"
  private String text;
}
