package com.ureca.ureca.domain.storage.controller;

import java.util.Set;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ureca.ureca.domain.storage.dto.PresignResult;
import com.ureca.ureca.domain.storage.service.StorageService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class StorageController {

  private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of("audio/mpeg", // mp3
      "audio/wav", // wav
      "audio/x-wav" // wav (browser compatibility)
  );

  private final StorageService storageService;

  @GetMapping("/presigned-url")
  public ResponseEntity<PresignResult> getUploadUrl(
      @RequestParam("contentType") String contentType) {

    if (!ALLOWED_CONTENT_TYPES.contains(contentType)) {
      return ResponseEntity.badRequest().build();
    }
    PresignResult response = storageService.generateUploadUrl(contentType);
    return ResponseEntity.ok(response);
  }
}
