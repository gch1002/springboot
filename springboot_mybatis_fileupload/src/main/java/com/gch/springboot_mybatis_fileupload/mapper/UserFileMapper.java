package com.gch.springboot_mybatis_fileupload.mapper;

import com.gch.springboot_mybatis_fileupload.domain.UserFile;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserFileMapper {
    List<UserFile> findByUserId(Integer id);

    //保存用户的文件记录
    void save(UserFile userFile);

    //根据id查文件
    UserFile findById(Integer id);

    //更新下载次数
    void update(UserFile userFile);

    void delete(Integer id);
}
