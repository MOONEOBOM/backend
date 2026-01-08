package com.ureca.ureca.global.common.response;

import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

  private final boolean success; // 항상 false
  private final String code; // 예: "VALIDATION_ERROR", "USER_NOT_FOUND"
  private final String message; // 대표 메시지
  private final LocalDateTime timestamp;
  private final String path; // 요청 path
  private final List<FieldError> errors; // validation detail

  @Getter
  @Builder
  public static class FieldError {
    private final String field;
    private final String reason;
  }

  public static ErrorResponse of(String code, String message, String path) {
    return ErrorResponse.builder().success(false).code(code).message(message)
        .timestamp(LocalDateTime.now()).path(path).build();
  }
}
