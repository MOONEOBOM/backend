package com.ureca.ureca.domain.storage.service;

import java.time.Duration;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.ureca.ureca.domain.storage.dto.PresignResult;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Service
@RequiredArgsConstructor
public class S3StorageService implements StorageService {

  private final S3Presigner s3Presigner;

  @Value("${ncp.object-storage.bucket}")
  private String bucket;

  @Override
  public PresignResult generateUploadUrl(String contentType) {
    // S3에 저장될 경로와 파일명 설정
    String ext = resolveExtension(contentType);

    String fileName = UUID.randomUUID() + ext;
    String dataKey = "input/" + fileName;

    PutObjectRequest objectRequest =
        PutObjectRequest.builder().bucket(bucket).key(dataKey).contentType(contentType).build();

    // 10분간 유효한 업로드용 URL 생성
    PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
        .signatureDuration(Duration.ofMinutes(10)).putObjectRequest(objectRequest).build();

    String url = s3Presigner.presignPutObject(presignRequest).url().toString();
    PresignResult presignResult = new PresignResult();
    presignResult.setDataKey(dataKey);
    presignResult.setUrl(url);

    return presignResult;
  }

  // 확장자 매핑
  private String resolveExtension(String contentType) {
    if (contentType == null || contentType.isBlank()) {
      return ""; // contentType이 비어있으면 확장자 없이 저장(비추천이지만 서버가 죽진 않게)
    }
    return switch (contentType) {
      case "audio/mpeg" -> ".mp3";
      case "audio/wav", "audio/wave", "audio/x-wav" -> ".wav";
      case "audio/mp4" -> ".m4a";
      case "audio/ogg" -> ".ogg";
      default -> ""; // 모르는 타입이면 확장자 없이
    };
  }
}
