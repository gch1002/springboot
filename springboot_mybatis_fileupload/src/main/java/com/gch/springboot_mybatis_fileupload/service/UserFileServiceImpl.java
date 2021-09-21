package com.gch.springboot_mybatis_fileupload.service;

import com.gch.springboot_mybatis_fileupload.domain.UserFile;
import com.gch.springboot_mybatis_fileupload.mapper.UserFileMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class UserFileServiceImpl implements UserFileService {
    @Resource
    private UserFileMapper userFileMapper;

    @Override
    public List<UserFile> findByUserId(Integer id) {
        return userFileMapper.findByUserId(id);
    }

    @Override
    public void save(UserFile userFile) {
        //是否是图片   解决？   判断类型中是否包含 image
        String isImg = userFile.getType().startsWith("image") ? "是" : "否";
        userFile.setIsImg(isImg);
        userFile.setDowncounts(0);
        userFile.setUploadTime(new Date());
        userFileMapper.save(userFile);
    }

    @Override
    public UserFile findByID(Integer id) {
        return userFileMapper.findById(id);
    }

    @Override
    public void update(UserFile userFile) {
        userFileMapper.update(userFile);
    }

    @Override
    public void delete(Integer id) {
        userFileMapper.delete(id);
    }
}
