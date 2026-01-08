package com.ureca.ureca.domain.auth.controller;

import com.ureca.ureca.domain.auth.service.AuthService;
import com.ureca.ureca.global.common.response.ApiResponse;
import com.ureca.ureca.global.util.CookieUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

  private static final String COOKIE_NAME = "2team_session";

  private final AuthService authService;

  @PostMapping("/login")
  public ApiResponse<Void> login(
      @RequestHeader("Authorization") String authorization,
      HttpServletResponse response
  ) throws Exception {
    String idToken = extractBearerToken(authorization);

    String sessionCookie = authService.issueSessionCookie(idToken);
    CookieUtil.addSessionCookie(
        response,
        COOKIE_NAME,
        sessionCookie,
        authService.sessionMaxAgeSeconds()
    );
    System.out.print("로그인?");
    return ApiResponse.ok("로그인 성공", null);
  }

  //  401 뜰 때 프론트가 호출해서 새 쿠키 받는 용도
  @PostMapping("/session")
  public ApiResponse<Void> refreshSession(
      @RequestHeader("Authorization") String authorization,
      HttpServletResponse response
  ) throws Exception {
    String idToken = extractBearerToken(authorization);

    String sessionCookie = authService.issueSessionCookie(idToken);
    CookieUtil.addSessionCookie(
        response,
        COOKIE_NAME,
        sessionCookie,
        authService.sessionMaxAgeSeconds()
    );

    return ApiResponse.ok();
  }

  @PostMapping("/logout")
  public ApiResponse<Void> logout(HttpServletResponse response) {
    CookieUtil.clearCookie(response, COOKIE_NAME);
    return ApiResponse.ok();
  }

  private String extractBearerToken(String authorization) {
    if (authorization == null || !authorization.startsWith("Bearer ")) {
      throw new IllegalArgumentException("Authorization Bearer 토큰이 필요합니다.");
    }
    return authorization.substring("Bearer ".length()).trim();
  }
}
