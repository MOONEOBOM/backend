package com.ureca.ureca.domain.summary.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SummaryRequestDto {
    private String category_label;  // 상담 분야
    private String stt_text;  // 전체 상담 텍스트 (txt)
    private List<String> keywords;  // 상담 내용 중 핵심 키워드 3개
}