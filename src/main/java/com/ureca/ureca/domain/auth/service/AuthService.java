package com.ureca.ureca.domain.auth.service;

import org.springframework.stereotype.Service;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.SessionCookieOptions;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final FirebaseAuth firebaseAuth;

  private static final long SESSION_EXPIRES_IN_MILLIS = 24L * 60 * 60 * 1000;

  public FirebaseToken verifyIdToken(String idToken) throws Exception {
    return firebaseAuth.verifyIdToken(idToken);
  }

  public String issueSessionCookie(String idToken) throws Exception {
    SessionCookieOptions options =
        SessionCookieOptions.builder().setExpiresIn(SESSION_EXPIRES_IN_MILLIS).build();

    return firebaseAuth.createSessionCookie(idToken, options);
  }

  public int sessionMaxAgeSeconds() {
    return (int) (SESSION_EXPIRES_IN_MILLIS / 1000);
  }
}
