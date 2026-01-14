package com.ureca.ureca.domain.auth.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.google.firebase.auth.FirebaseToken;
import com.ureca.ureca.domain.auth.service.AuthService;
import com.ureca.ureca.domain.user.dto.User;
import com.ureca.ureca.domain.user.service.UserService;
import com.ureca.ureca.global.common.exception.BusinessException;
import com.ureca.ureca.global.common.exception.ErrorCode;
import com.ureca.ureca.global.common.response.ApiResponse;
import com.ureca.ureca.global.util.CookieUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

  private static final String COOKIE_NAME = "2team_session";

  private final AuthService authService;
  private final UserService userService;

  @PostMapping("/login")
  public ApiResponse<Void> login(@RequestHeader("Authorization") String authorization,
      HttpServletResponse response) throws Exception {
    String idToken = extractBearerToken(authorization);

    // 1) Firebase ID Token 검증 + 사용자 정보 추출
    FirebaseToken decoded = authService.verifyIdToken(idToken);

    String firebaseUid = decoded.getUid();
    String email = decoded.getEmail();
    String name = decoded.getName(); // 구글 계정 표시명
    String photoUrl = decoded.getPicture();

    // 2) 서버 세션쿠키 발급
    String sessionCookie = authService.issueSessionCookie(idToken);
    CookieUtil.addSessionCookie(response, COOKIE_NAME, sessionCookie,
        authService.sessionMaxAgeSeconds());

    // 3) DB upsert
    User user = userService.upsertFirebaseUser(firebaseUid, email, name, photoUrl);

    return ApiResponse.ok("로그인 성공", null);
  }

  // 401 뜰 때 프론트가 호출해서 새 쿠키 받는 용도
  @PostMapping("/session")
  public ApiResponse<Void> refreshSession(@RequestHeader("Authorization") String authorization,
      HttpServletResponse response) throws Exception {
    String idToken = extractBearerToken(authorization);

    // 토큰검증
    authService.verifyIdToken(idToken);

    String sessionCookie = authService.issueSessionCookie(idToken);
    CookieUtil.addSessionCookie(response, COOKIE_NAME, sessionCookie,
        authService.sessionMaxAgeSeconds());

    return ApiResponse.ok();
  }

  @PostMapping("/logout")
  public ApiResponse<Void> logout(HttpServletResponse response) {
    CookieUtil.clearCookie(response, COOKIE_NAME);
    return ApiResponse.ok();
  }

  private String extractBearerToken(String authorization) {
    if (authorization == null || !authorization.startsWith("Bearer ")) {
      throw new BusinessException(ErrorCode.UNAUTHORIZED, "Authorization Bearer 토큰이 필요합니다.");
    }
    return authorization.substring("Bearer ".length()).trim();
  }
}
