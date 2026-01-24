package com.ureca.ureca.domain.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.ureca.ureca.domain.user.dto.User;

@Mapper
public interface UserMapper {
  User findById(@Param("id") Long id);

  // uid로 조회
  User findByFirebaseUid(@Param("firebaseUid") String firebaseUid);

  // 신규 유저 insert
  int insertFirebaseUser(User user);

  // 기존 유저 업데이트(이름/사진/last_login)
  int updateFirebaseUser(User user);
  
  // 온보딩 완료 업데이트
  int completeFirstLogin(@Param("userId") Long userId);
}
