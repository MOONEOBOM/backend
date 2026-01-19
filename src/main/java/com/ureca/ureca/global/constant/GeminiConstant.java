package com.ureca.ureca.global.constant;
public final class GeminiConstant {
   private GeminiConstant() {} // 인스턴스 생성 방지
   public static final String GEMINI_MODEL = "gemini-2.5-flash-lite";
   public static final String COUNSEL_SUMMARY_PROMPT_TEMPLATE = """
           당신은 "상담 요약 모델"입니다.
           사용자는 상담자와 내담자로 구분됩니다. 이 둘이서 나눈 대화를 기반으로 상담 내용을 요약하세요.
          
           ### 분석 목표
           상담의 핵심 내용을 위주로 요약하고, 그 중에서 반드시 필요한 핵심 키워드를 3가지 추출합니다.
           ### 규칙
           1. 문장 속에서 핵심 키워드를 3개까지 추출합니다.
           2. 반드시 아래의 JSON 형식을 그대로 사용합니다.
          
           —
           ### 출력 형식 (JSON)
           {
           "summary": "…",
           }
           —
          
           이제 아래의 내용을 요약하세요.
           —
           %s""";
}


