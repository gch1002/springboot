package com.gch.springboot_mybatis_fileupload.service;

import com.gch.springboot_mybatis_fileupload.domain.User;
import com.gch.springboot_mybatis_fileupload.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public User login(User user) {
        return userMapper.login(user);
    }
}
