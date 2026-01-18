package com.ureca.ureca.domain.scenario.dto;

import java.util.List;

import com.ureca.ureca.domain.scenario.dto.ClovaResponse.Speaker;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 클라이언트용 STT 결과 한 줄 DTO (순서 유지)
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SttResultDto   {
    private String fullText;               // 전체 문자열
    private Double confidence;             // 전체 confidence (있으면)
    private List<SpeakerLineDto> lines;    // A/B 한 줄씩(순서 유지)

    
    @Data
    public static class SpeakerLineDto {
        private String speaker;   // "A" / "B"
        private String text;      // 문장
        private Integer startMs;  // ms
        private Integer endMs;    // ms
        private Double confidence;
    }
}


