package com.ureca.ureca.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
  private Long id;
  private String firebaseUid;
  private String email;
  private String name;
  private String photoUrl;
  private boolean firstLogin;
  private java.time.LocalDateTime lastLoginAt;
}
