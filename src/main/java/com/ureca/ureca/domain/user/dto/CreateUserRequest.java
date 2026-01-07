package com.ureca.ureca.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequest {

  @NotBlank(message = "email은 필수입니다.")
  @Email(message = "email 형식이 올바르지 않습니다.")
  private String email;

  @NotBlank(message = "name은 필수입니다.")
  private String name;
}
