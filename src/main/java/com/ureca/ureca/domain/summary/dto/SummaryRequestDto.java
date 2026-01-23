package com.ureca.ureca.domain.summary.dto;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SummaryRequestDto {
  @JsonProperty("conversation")
  private List<ChatMessage> messages;

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ChatMessage {
    private String role; // agent | user
    @JsonAlias("text")
    private String message;
  }
}
