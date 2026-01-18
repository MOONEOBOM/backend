package com.ureca.ureca.domain.storage.controller;

import com.ureca.ureca.domain.storage.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class StorageController {

    private final StorageService storageService;

    @GetMapping("/presigned-url")
    public ResponseEntity<Map<String, String>> getUploadUrl(@RequestParam("fileName") String fileName) {
        String url = storageService.generateUploadUrl(fileName);

        Map<String, String> response = new HashMap<>();
        response.put("url", url);

        return ResponseEntity.ok(response);
    }
}