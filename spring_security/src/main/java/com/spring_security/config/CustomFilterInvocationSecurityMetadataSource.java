package com.spring_security.config;

import com.spring_security.domain.Menu;
import com.spring_security.domain.Role;
import com.spring_security.mapper.MenuMappper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;
import java.util.List;

@Component
public class CustomFilterInvocationSecurityMetadataSource implements org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource {
    private AntPathMatcher matcher = new AntPathMatcher();
    @Autowired
    private MenuMappper menuMappper;

    /**
     * 获得当前URL所需要的角色
     *
     * @param o
     * @return
     * @throws IllegalArgumentException
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        FilterInvocation invocation = (FilterInvocation) o;
        //获得当前请求的URL
        String requestUrl = invocation.getRequestUrl();
        List<Menu> menus = menuMappper.getAllMenus();
        //循环所以的Menu
        for (Menu menu : menus) {
            //每个菜单的Ant风格的请求URL和实际的URL匹配
            if (matcher.match(menu.getPattern(), requestUrl)) {
                //获得匹配上的菜单的权限
                List<Role> roles = menu.getRoles();
                String[] rolesAttributes = new String[roles.size()];
                for (int i = 0; i < rolesAttributes.length; i++) {
                    rolesAttributes[i] = roles.get(i).getName();
                }
                //将权限的集合存放到ConfigAttribute集合中
                return SecurityConfig.createList(rolesAttributes);
            }
        }
        //如果没有匹配上放一个标志位
        return SecurityConfig.createList("ROLE_LOGIN");
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return FilterInvocation.class.isAssignableFrom(aClass);
    }
}