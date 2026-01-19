package com.ureca.ureca.domain.storage.service;

import com.ureca.ureca.domain.storage.dto.PresignResult;

public interface StorageService {
  PresignResult generateUploadUrl(String contentType);
}
