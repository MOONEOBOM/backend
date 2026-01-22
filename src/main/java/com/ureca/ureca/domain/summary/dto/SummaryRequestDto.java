package com.ureca.ureca.domain.summary.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SummaryRequestDto {
	@JsonProperty("conversation")
    private List<ChatMessage> messages;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatMessage {
        private String speaker;
        private String message;
    }
}