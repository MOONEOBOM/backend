package com.ureca.ureca.domain.auth.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.SessionCookieOptions;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final FirebaseAuth firebaseAuth;

  private static final long SESSION_EXPIRES_IN_MILLIS = 24L * 60 * 60 * 1000;

  public String issueSessionCookie(String idToken) throws Exception {
    FirebaseToken decoded = firebaseAuth.verifyIdToken(idToken);
    
    SessionCookieOptions options = SessionCookieOptions.builder()
            .setExpiresIn(SESSION_EXPIRES_IN_MILLIS)
            .build();
    
    return firebaseAuth.createSessionCookie(idToken, options);


  }

  public int sessionMaxAgeSeconds() {
    return (int) (SESSION_EXPIRES_IN_MILLIS / 1000);
  }
}
