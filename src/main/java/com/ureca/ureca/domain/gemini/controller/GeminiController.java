package com.ureca.ureca.domain.gemini.controller;

import com.ureca.ureca.domain.gemini.service.GeminiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GeminiController {

    private final GeminiService geminiService;

    @GetMapping("/api/gemini/test")
    public String test() {
        geminiService.testGemini();
        return "콘솔을 확인하세요!";
    }
}
