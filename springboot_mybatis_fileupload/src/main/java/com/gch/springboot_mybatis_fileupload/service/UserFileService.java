package com.gch.springboot_mybatis_fileupload.service;

import com.gch.springboot_mybatis_fileupload.domain.UserFile;

import java.util.List;

public interface UserFileService {
    List<UserFile> findByUserId(Integer id);

    void save(UserFile userFile);

    UserFile findByID(Integer id);

    void update(UserFile userFile);

    void delete(Integer id);
}
