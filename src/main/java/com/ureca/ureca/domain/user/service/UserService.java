package com.ureca.ureca.domain.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ureca.ureca.domain.user.dto.User;
import com.ureca.ureca.domain.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserMapper userMapper;

  public User getUser(Long id) {
    User user = userMapper.findById(id);
    if (user == null)
      throw new IllegalArgumentException("유저가 존재하지 않습니다. id=" + id);
    return user;
  }

  @Transactional
  public void createUser(String email, String name) {
    userMapper.insertUser(email, name);
  }
}
