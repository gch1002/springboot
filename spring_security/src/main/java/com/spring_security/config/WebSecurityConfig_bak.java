package com.spring_security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

/*@Configuration*/
public class WebSecurityConfig_bak extends WebSecurityConfigurerAdapter {
    @Bean
    PasswordEncoder passwordEncoder() {
        //过时的对象，表示用户密码不加密aaa
        return NoOpPasswordEncoder.getInstance();
    }

    /**
     * 用户认证的方法
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //开启基于内存的认证
        auth.inMemoryAuthentication()
                //在内存中创建一个用户admin
                .withUser("admin")
                //为admin用户设置密码
                .password("123")
                //为admin用户设置2个角色 ADMIN、USER
                .roles("ADMIN", "USER")
                .and()
                .withUser("user")
                .password("123")
                .roles("USER")
                .and()
                .withUser("dba")
                .password("123")
                .roles("DBA");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //开启HttpSecurity的配置
        http.authorizeRequests()
                //描述资源的URL的路径匹配
                .antMatchers("/admin/**")
                //具备ADMIN的角色
                .hasRole("ADMIN")
                .antMatchers("/user/**")
                .access("hasAnyRole('USER','ADMIN')")
                .antMatchers("/dba/**")
                .hasRole("DBA")
                //剩下的资源
                .anyRequest()
                //必须开启认证
                .authenticated()
                .and()
                .formLogin()
                //自定义的登录页面
                .loginPage("/login_page")
                //如果表单中的登录名不是username，可以通过这个参数定制登录名的name属性
                .usernameParameter("uname")
                .passwordParameter("pwd")
                //登录的url为/doLogin
                .loginProcessingUrl("/doLogin")
                //处理登录成功的逻辑
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request,
                                                        HttpServletResponse response,
                                                        Authentication authentication) throws IOException, ServletException {
                        //如何在该方法中返回JSON
                        response.setContentType("application/json;charset=UTF-8");
                        HashMap<String, Object> map = new HashMap<>();
                        Object user = authentication.getPrincipal();
                        map.put("user", user);
                        map.put("status", 200);
                        //map对象转成JSON
                        ObjectMapper objectMapper = new ObjectMapper();
                        String json = objectMapper.writeValueAsString(map);
                        PrintWriter writer = response.getWriter();
                        writer.write(json);
                        writer.flush();
                        writer.close();
                    }
                })
                //处理登录失败的逻辑
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest request,
                                                        HttpServletResponse response,
                                                        AuthenticationException e) throws IOException, ServletException {
                        response.setContentType("application/json;charset=UTF-8");
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("status", 400);
                        //登录失败的原因：
                        if (e instanceof LockedException) {
                            map.put("msg", "账号被锁定，请联系管理员");
                        } else if (e instanceof DisabledException) {
                            map.put("msg", "账号被禁用，请联系管理员");
                        } else if (e instanceof CredentialsExpiredException) {
                            map.put("msg", "密码过期，请联系管理员");
                        } else if (e instanceof AccountExpiredException) {
                            map.put("msg", "账号过期，请联系管理员");
                        } else if (e instanceof BadCredentialsException) {
                            map.put("msg", "用户名或者密码错误");
                        } else {
                            map.put("msg", "未知异常");
                        }
                        ObjectMapper objectMapper = new ObjectMapper();
                        String json = objectMapper.writeValueAsString(map);
                        PrintWriter writer = response.getWriter();
                        writer.write(json);
                        writer.flush();
                        writer.close();
                    }
                })
                //对于表单的登录给与放行
                .permitAll()
                .and()
                //注销登录
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(new LogoutSuccessHandler() {
                    @Override
                    public void onLogoutSuccess(HttpServletRequest request,
                                                HttpServletResponse response,
                                                Authentication authentication) throws IOException, ServletException {
                        response.setContentType("application/json;charset=UTF-8");
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("msg", "注销成功");
                        map.put("status", 200);
                        ObjectMapper objectMapper = new ObjectMapper();
                        String json = objectMapper.writeValueAsString(map);
                        PrintWriter writer = response.getWriter();
                        writer.write(json);
                        writer.flush();
                        writer.close();
                    }
                })
                .and()
                //对于跨站的csrf攻击关闭，如果不关闭，无法通过postman工具去厕所
                .csrf()
                .disable();
    }
}
