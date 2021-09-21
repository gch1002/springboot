package com.gch.springboot_mybatis_fileupload.mapper;

import com.gch.springboot_mybatis_fileupload.domain.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    User login(User user);
}
