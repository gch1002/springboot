package com.spring_security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring_security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserService userService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    CustomAccessDecisionManager customAccessDecisionManager;
    @Autowired
    CustomFilterInvocationSecurityMetadataSource customFilterInvocationSecurityMetadataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //开启HttpSecurity的配置
        http.authorizeRequests()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                        object.setAccessDecisionManager(customAccessDecisionManager);
                        object.setSecurityMetadataSource(customFilterInvocationSecurityMetadataSource);
                        return object;
                    }
                })
                //剩下的资源
                .anyRequest()
                //必须认证（登录后）访问
                .authenticated()
                .and()
                //开启表单登录
                .formLogin()
                //设置自定义登录页URL,当前后端分离jSON登录的时候，不需要返回试图，建议不要设置自动登录页
                .loginPage("/login_page")
                // 设置表单中用户名的name名称,默认是username
                .usernameParameter("username")
                //设置表单中密码的name名称，默认是password
                .passwordParameter("password")
                //登录的url为/doLogin
                .loginProcessingUrl("/doLogin")
                //处理登录成功的逻辑
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request,
                                                        HttpServletResponse response, Authentication auth) throws IOException, ServletException {
                        //设置JSON响应
                        response.setContentType("application/json;charset=UTF-8");
                        Object principal = auth.getPrincipal();
                        Map<String, Object> map = new HashMap<>();
                        map.put("msg", principal);
                        map.put("status", 200);
                        PrintWriter out = response.getWriter();
                        out.println(new ObjectMapper().writeValueAsString(map));
                        out.flush();
                        out.close();
                    }
                })
                //处理登录失败的逻辑
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response
                            , AuthenticationException e) throws IOException, ServletException {
                        //设置JSON响应
                        response.setContentType("application/json;charset=UTF-8");
                        PrintWriter out = response.getWriter();
                        Map<String, Object> map = new HashMap<>();
                        map.put("status", 401);
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
                            map.put("msg", "未知原因，登录失败");
                        }
                        out.write(new ObjectMapper().writeValueAsString(map));
                        out.flush();
                        out.close();
                    }
                })
                //开启登录的接口不需要认证
                .permitAll()
                .and()
                //设置注销
                .logout()
                .logoutUrl("/logout")
                //处理注销成功逻辑
                .logoutSuccessHandler(new LogoutSuccessHandler() {
                    @Override
                    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                                Authentication auth) throws IOException, ServletException {

                        response.setContentType("application/json;charset=UTF-8");
                        PrintWriter out = response.getWriter();
                        Map<String, Object> map = new HashMap<>();
                        map.put("status", 200);
                        map.put("msg", "注销成功");
                        out.write(new ObjectMapper().writeValueAsString(map));
                        out.flush();
                        out.close();

                    }
                })
                .permitAll()
                .and()
                //关闭csrf，否则会认为是跨站攻击导致无法测试
                .csrf()
                .disable();
    }
}
