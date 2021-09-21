package com.gch.springboot_mybatis_fileupload;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.gch.springboot_mybatis_fileupload.mapper")
public class SpringbootMybatisFileuploadApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringbootMybatisFileuploadApplication.class, args);
    }

}
