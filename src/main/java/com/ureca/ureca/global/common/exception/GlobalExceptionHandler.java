package com.ureca.ureca.global.common.exception;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import com.ureca.ureca.global.common.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e,
      HttpServletRequest request) {
    ErrorCode errorCode = e.getErrorCode();
    ErrorResponse body = ErrorResponse.of(errorCode.getCode(), e.getMessage(), // 커스텀 메시지 가능
        request.getRequestURI());
    return ResponseEntity.status(errorCode.getStatus()).body(body);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(
      MethodArgumentTypeMismatchException e, HttpServletRequest request) {
    String paramName = e.getName();
    String requiredType =
        (e.getRequiredType() != null) ? e.getRequiredType().getSimpleName() : "unknown";
    String value = (e.getValue() != null) ? e.getValue().toString() : "null";

    ErrorResponse body =
        ErrorResponse.builder().success(false).code(ErrorCode.INVALID_REQUEST.getCode())
            .message(String.format("'%s' 값이 올바르지 않습니다. (value=%s, requiredType=%s)", paramName,
                value, requiredType))
            .timestamp(java.time.LocalDateTime.now()).path(request.getRequestURI()).build();

    return ResponseEntity.status(ErrorCode.INVALID_REQUEST.getStatus()).body(body);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(
      HttpMessageNotReadableException e, HttpServletRequest request) {
    ErrorResponse body = ErrorResponse.builder().success(false)
        .code(ErrorCode.INVALID_REQUEST.getCode()).message("요청 본문(JSON) 형식이 올바르지 않습니다.")
        .timestamp(java.time.LocalDateTime.now()).path(request.getRequestURI()).build();

    return ResponseEntity.status(ErrorCode.INVALID_REQUEST.getStatus()).body(body);
  }

  // DB 제약 조건 위반 (UNIQUE, FK, NOT NULL 등)
  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(
      DataIntegrityViolationException e, HttpServletRequest request) {

    ErrorResponse body =
        ErrorResponse.builder().success(false).code(ErrorCode.DB_INTEGRITY_VIOLATION.getCode())
            .message(ErrorCode.DB_INTEGRITY_VIOLATION.getMessage())
            .timestamp(java.time.LocalDateTime.now()).path(request.getRequestURI()).build();

    return ResponseEntity.status(ErrorCode.DB_INTEGRITY_VIOLATION.getStatus()).body(body);
  }

  // @Valid DTO 검증 실패
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
      MethodArgumentNotValidException e, HttpServletRequest request) {
    List<ErrorResponse.FieldError> errors = e
        .getBindingResult().getFieldErrors().stream().map(fe -> ErrorResponse.FieldError.builder()
            .field(fe.getField()).reason(fe.getDefaultMessage()).build())
        .collect(Collectors.toList());

    ErrorResponse body = ErrorResponse.builder().success(false)
        .code(ErrorCode.VALIDATION_ERROR.getCode()).message(ErrorCode.VALIDATION_ERROR.getMessage())
        .timestamp(java.time.LocalDateTime.now()).path(request.getRequestURI()).errors(errors)
        .build();

    return ResponseEntity.status(ErrorCode.VALIDATION_ERROR.getStatus()).body(body);
  }

  // @ModelAttribute / query param 바인딩 실패 등
  @ExceptionHandler(BindException.class)
  public ResponseEntity<ErrorResponse> handleBindException(BindException e,
      HttpServletRequest request) {
    ErrorResponse body = ErrorResponse.of(ErrorCode.INVALID_REQUEST.getCode(),
        "요청 파라미터 형식이 올바르지 않습니다.", request.getRequestURI());
    return ResponseEntity.status(ErrorCode.INVALID_REQUEST.getStatus()).body(body);
  }

  // 나머지 (진짜 서버 에러)
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception e, HttpServletRequest request) {
    // 운영에서는 로그만 남기고 message는 고정 추천
    ErrorResponse body = ErrorResponse.of(ErrorCode.INTERNAL_ERROR.getCode(),
        ErrorCode.INTERNAL_ERROR.getMessage(), request.getRequestURI());
    return ResponseEntity.status(ErrorCode.INTERNAL_ERROR.getStatus()).body(body);
  }
}
