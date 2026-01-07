package com.ureca.ureca.domain.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ureca.ureca.domain.user.dto.CreateUserRequest;
import com.ureca.ureca.domain.user.dto.User;
import com.ureca.ureca.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
  private final UserService userService;

  @GetMapping("/{id}")
  public User get(@PathVariable("id") Long id) {
    return userService.getUser(id);
  }

  @PostMapping
  public void create(@Valid @RequestBody CreateUserRequest req) {
    userService.createUser(req.getEmail(), req.getName());
  }
}
