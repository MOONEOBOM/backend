package com.ureca.ureca.domain.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.ureca.ureca.domain.user.dto.User;

@Mapper
public interface UserMapper {
  User findById(@Param("id") Long id);
  int insertUser(@Param("email") String email, @Param("name") String name);
}
