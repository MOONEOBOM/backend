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
public class CallMessagesResponseDto {

  private Long id;
  private Integer seq;
  private String role;
  private String message;
  private LocalDateTime createdAt;
}

