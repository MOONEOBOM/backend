package com.ureca.ureca.domain.scenario.dto;

import java.util.List;

import lombok.Data;

@Data
public class ClovaResponse {

    /** COMPLETED / FAILED */
    private String result;

    /** Succeeded / error message */
    private String message;

    /** 전체 변환된 텍스트 */
    private String text;

    /** 전체 confidence */
    private Double confidence;

    /** 화자/문장 단위 결과 (시간 순) */
    private List<Segment> segments;

    // ================= nested =================

    @Data
    public static class Segment {
        private Integer start;      // ms
        private Integer end;        // ms
        private String text;        // 문장
        private Double confidence;  // 문장 정확도
        private Speaker speaker;    // 화자 정보
    }

    @Data
    public static class Speaker {
        private String label; // "1", "2"
        private String name;  // "A", "B"
    }
}