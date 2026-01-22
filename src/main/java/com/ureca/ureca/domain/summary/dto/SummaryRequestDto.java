package com.ureca.ureca.domain.summary.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

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
        private String speaker;
        @JsonAlias("text")
        private String message;
    }
}