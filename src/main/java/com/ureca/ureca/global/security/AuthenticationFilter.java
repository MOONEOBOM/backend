package com.ureca.ureca.global.security;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

  private static final String COOKIE_NAME = "2team_session";

  private final FirebaseAuth firebaseAuth;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain
  ) throws java.io.IOException, jakarta.servlet.ServletException {

    String sessionCookie = extractCookie(request, COOKIE_NAME);

    if (sessionCookie != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      try {
        // Firebase Session Cookie 검증
        FirebaseToken decoded = firebaseAuth.verifySessionCookie(sessionCookie, false);

        // 최소 인증정보: uid를 principal로
        var auth = new UsernamePasswordAuthenticationToken(
            decoded.getUid(),
            null,
            Collections.emptyList()
        );

        SecurityContextHolder.getContext().setAuthentication(auth);
      } catch (Exception ex) {
        SecurityContextHolder.clearContext();
      }
    }

    filterChain.doFilter(request, response);
  }

  private String extractCookie(HttpServletRequest request, String name) {
    Cookie[] cookies = request.getCookies();
    if (cookies == null) return null;

    for (Cookie c : cookies) {
      if (name.equals(c.getName())) return c.getValue();
    }
    return null;
  }
}