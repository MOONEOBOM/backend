
package com.ureca.ureca.domain.gemini.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GeminiRequestDto {

    private List<Content> contents;

    /** 텍스트 전송하는 생성자 */
    public GeminiRequestDto(String text) {
        this.contents = List.of(
                new Content(
                        List.of(new TextPart(text))
                )
        );
    }

   
    public static GeminiRequestDto ofText(String text) {
        return new GeminiRequestDto(text);
    }


    /* ----- 내부 DTO ----- */

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Content {
        private List<TextPart> parts;
    }



    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class TextPart {
        private String text;
    }



}

