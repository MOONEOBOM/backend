package com.ureca.ureca.domain.summary.dto;

import java.util.List;
import lombok.Data;

@Data
public class SummaryResponseDto {
    private String title;  // 요약본 제목 (카드 타이틀)
    private String summary;  // 상담 요약본 (3 ~ 4줄)
    private List<KeywordCheck> keyword_check;  // 키워드들이 얼마나 잘 나왔는지
    private List<TodoItem> todo_list;  // 상담 후 할 일 목록

    @Data
    public static class KeywordCheck {
        private String keyword;  // 시나리오에서 뽑은 키워드 3개
        @com.fasterxml.jackson.annotation.JsonProperty("is_spoken")  // 언더바(_) 대체 부분 추가
        private boolean spoken;  // 해당 키워드들이 얼마나 잘 나왔는지
    }

    @Data
    public static class TodoItem {
        private String todobuttontext;  // 해야할 일 텍스트 (버튼에 표시해주는 것)
        private String url;  // 해야할 일 url
        private String description;  // 해야할 일에 대한 설명
    }
}