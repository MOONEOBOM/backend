package com.ureca.ureca.global.common.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
  // Common
  INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR",
      "서버 오류가 발생했습니다."), INVALID_REQUEST(HttpStatus.BAD_REQUEST, "INVALID_REQUEST",
          "잘못된 요청입니다."), UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED",
              "인증이 필요합니다."), FORBIDDEN(HttpStatus.FORBIDDEN, "FORBIDDEN",
                  "권한이 없습니다."), DB_INTEGRITY_VIOLATION(HttpStatus.CONFLICT,
                      "DB_INTEGRITY_VIOLATION", "충돌이 발생하였습니다."),
  // Validation
  VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "요청 값이 올바르지 않습니다."),

  // Example Domain
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "사용자를 찾을 수 없습니다."),

  // STT
  SCENARIO_INVALID_DATA_KEY(HttpStatus.BAD_REQUEST, "SCENARIO_INVALID_DATA_KEY",
      "dataKey가 올바르지 않습니다."), SCENARIO_INVALID_URL(HttpStatus.BAD_REQUEST, "SCENARIO_INVALID_URL",
          "url이 올바르지 않습니다."),

  STT_FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "STT_FILE_NOT_FOUND",
      "해당 음성 파일을 찾을 수 없습니다."), SEGMENTS_NOT_FOUND(HttpStatus.NOT_FOUND, "SEGMENTS_NOT_FOUND",
          "해당 음성 파일에 세그먼트를 찾을 수 없습니다."),

  CLOVA_API_ERROR(HttpStatus.BAD_GATEWAY, "CLOVA_API_ERROR",
      "STT 외부 서비스 호출에 실패했습니다."), CLOVA_API_TIMEOUT(HttpStatus.GATEWAY_TIMEOUT, "CLOVA_API_TIMEOUT",
          "STT 외부 서비스 응답이 지연되고 있습니다."), CLOVA_API_UNAUTHORIZED(HttpStatus.BAD_GATEWAY,
              "CLOVA_API_UNAUTHORIZED", "STT 외부 서비스 인증에 실패했습니다."), CLOVA_API_RATE_LIMIT(
                  HttpStatus.TOO_MANY_REQUESTS, "CLOVA_API_RATE_LIMIT", "STT 외부 서비스 호출 제한에 걸렸습니다."),

  CLOVA_RESPONSE_PARSE_FAILED(HttpStatus.BAD_GATEWAY, "CLOVA_RESPONSE_PARSE_FAILED",
      "STT 응답을 처리할 수 없습니다."),
  
  GEMINI_EMPTY_RESPONSE(HttpStatus.BAD_GATEWAY, "GEMINI_EMPTY_RESPONSE", "Gemini 응답이 비어 있습니다."),
  GEMINI_RESPONSE_PARSE_FAILED(HttpStatus.BAD_GATEWAY, "GEMINI_RESPONSE_PARSE_FAILED", "JSON 파싱 실패로 Gemini 응답 데이터를 처리할 수 없습니다."),
  
	NULL_REFERENCE_ERROR(HttpStatus.BAD_REQUEST, "NULL_REFERENCE_ERROR", "참조된 요청 객체가 null입니다."),
  
  REQUIRED_FIELD_MISSING(HttpStatus.BAD_REQUEST, "REQUIRED_FIELD_MISSING", "필수 입력 필드값이 누락되었습니다.");


  private final HttpStatus status;
  private final String code;
  private final String message;

  ErrorCode(HttpStatus status, String code, String message) {
    this.status = status;
    this.code = code;
    this.message = message;
  }

  public HttpStatus getStatus() {
    return status;
  }

  public String getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }
}
