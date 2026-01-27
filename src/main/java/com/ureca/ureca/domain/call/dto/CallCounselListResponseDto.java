package com.ureca.ureca.domain.call.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CallCounselListResponseDto {

  private Long id;
  private String previewText;
  private LocalDateTime startedAt;
}

