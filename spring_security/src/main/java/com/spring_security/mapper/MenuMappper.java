package com.spring_security.mapper;

import com.spring_security.domain.Menu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MenuMappper {
    List<Menu> getAllMenus();
}
