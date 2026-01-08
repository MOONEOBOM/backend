package com.ureca.ureca.domain.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ureca.ureca.domain.user.dto.User;
import com.ureca.ureca.domain.user.mapper.UserMapper;
import com.ureca.ureca.global.common.exception.BusinessException;
import com.ureca.ureca.global.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserMapper userMapper;

  public User getUser(Long id) {
    User user = userMapper.findById(id);
    if (user == null)
      throw new BusinessException(ErrorCode.USER_NOT_FOUND);
    return user;
  }

  @Transactional
  public void createUser(String email, String name) {
    userMapper.insertUser(email, name);
  }
}
