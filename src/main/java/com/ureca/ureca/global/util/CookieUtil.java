package com.ureca.ureca.global.util;

import jakarta.servlet.http.HttpServletResponse;

public class CookieUtil {

  private CookieUtil() {}

  public static void addSessionCookie(
      HttpServletResponse response,
      String cookieName,
      String cookieValue,
      int maxAgeSeconds
  ) {
    String cookie = String.format(
        "%s=%s; Path=/; Max-Age=%d; HttpOnly; SameSite=Lax",
        cookieName, cookieValue, maxAgeSeconds
    );

    // 로컬 http니까 Secure는 붙이지 않음
    response.addHeader("Set-Cookie", cookie);
  }

  public static void clearCookie(HttpServletResponse response, String cookieName) {
    String cookie = String.format(
        "%s=; Path=/; Max-Age=0; HttpOnly; SameSite=Lax",
        cookieName
    );
    response.addHeader("Set-Cookie", cookie);
  }
}
