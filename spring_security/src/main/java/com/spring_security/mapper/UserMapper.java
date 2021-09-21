package com.spring_security.mapper;

import com.spring_security.domain.Role;
import com.spring_security.domain.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    User loadUserByUsername(String username);

    List<Role> getUserRolesByUid(Integer id);
}
