package com.ureca.ureca.domain.user.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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

  @GetMapping("/me")
  public ApiResponse<User> me(Authentication authentication) {
    String firebaseUid = (String) authentication.getPrincipal(); // 필터에서 uid 넣어둠
    User user = userService.getByFirebaseUid(firebaseUid);
    
    return ApiResponse.ok("유저 조회 성공",user);
  }
  
  @PatchMapping("/complete")
  public ApiResponse<Void> updateFirstLogin(Authentication authentication) {
	 String firebaseUid = (String) authentication.getPrincipal(); // 필터에서 uid 넣어둠
	 User user = userService.getByFirebaseUid(firebaseUid);
	 
	 userService.completeFirstLogin(user.getId());
	  
	 return ApiResponse.ok();
  }
  
}
