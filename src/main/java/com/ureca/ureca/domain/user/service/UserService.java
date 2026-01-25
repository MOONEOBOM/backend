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
  public User upsertFirebaseUser(String firebaseUid, String email, String name, String photoUrl) {
    User existing = userMapper.findByFirebaseUid(firebaseUid);

    if (existing == null) {
      User created = User.builder().firebaseUid(firebaseUid).email(email).name(name)
          .photoUrl(photoUrl).build();
      userMapper.insertFirebaseUser(created);
      return userMapper.findByFirebaseUid(firebaseUid);
    }

    // 기존 유저면 프로필 최신화(이름/사진/last_login)
    User toUpdate = User.builder().id(existing.getId()).firebaseUid(firebaseUid)
        .email(email != null ? email : existing.getEmail())
        .name(name != null ? name : existing.getName()).photoUrl(photoUrl).build();

    userMapper.updateFirebaseUser(toUpdate);
    return userMapper.findByFirebaseUid(firebaseUid);
  }

  // /users/me 용
  public User getByFirebaseUid(String firebaseUid) {
    User user = userMapper.findByFirebaseUid(firebaseUid);
    if (user == null)
      throw new BusinessException(ErrorCode.USER_NOT_FOUND);
    return user;
  }
  
  @Transactional
  public void completeFirstLogin(Long userId) {

    int updated = userMapper.completeFirstLogin(userId);

    if (updated == 0) {
    	// 이미 완료된 경우를 허용: 존재 여부만 확인
    	if (userMapper.findById(userId) == null) {
    		throw new BusinessException(ErrorCode.USER_NOT_FOUND);
    	}
    	return;
    }
  }
}
