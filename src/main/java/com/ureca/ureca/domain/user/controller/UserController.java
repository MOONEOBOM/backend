package com.ureca.ureca.domain.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ureca.ureca.domain.user.dto.User;
import com.ureca.ureca.domain.user.service.UserService;
import com.ureca.ureca.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
  private final UserService userService;

  @GetMapping("/{id}")
  public ApiResponse<User> get(@PathVariable("id") Long id) {
    User data = userService.getUser(id);
    return ApiResponse.ok(data);
  }
}
