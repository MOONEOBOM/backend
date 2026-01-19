package com.ureca.ureca.global.api.gemini;

import com.ureca.ureca.domain.gemini.dto.request.GeminiRequestDto;
import com.ureca.ureca.domain.gemini.dto.response.GeminiResponseDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange("/v1beta/models/")
public interface GeminiInterface {

    @PostExchange("{model}:generateContent")
    GeminiResponseDto getCompletion(
            @PathVariable("model") String model,
            @RequestBody GeminiRequestDto request
    );
}
