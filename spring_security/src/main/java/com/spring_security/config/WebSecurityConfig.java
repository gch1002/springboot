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
        //??????HttpSecurity?????????
        http.authorizeRequests()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                        object.setAccessDecisionManager(customAccessDecisionManager);
                        object.setSecurityMetadataSource(customFilterInvocationSecurityMetadataSource);
                        return object;
                    }
                })
                //???????????????
                .anyRequest()
                //?????????????????????????????????
                .authenticated()
                .and()
                //??????????????????
                .formLogin()
                //????????????????????????URL,??????????????????jSON???????????????????????????????????????????????????????????????????????????
                .loginPage("/login_page")
                // ???????????????????????????name??????,?????????username
                .usernameParameter("username")
                //????????????????????????name??????????????????password
                .passwordParameter("password")
                //?????????url???/doLogin
                .loginProcessingUrl("/doLogin")
                //???????????????????????????
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request,
                                                        HttpServletResponse response, Authentication auth) throws IOException, ServletException {
                        //??????JSON??????
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
                //???????????????????????????
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response
                            , AuthenticationException e) throws IOException, ServletException {
                        //??????JSON??????
                        response.setContentType("application/json;charset=UTF-8");
                        PrintWriter out = response.getWriter();
                        Map<String, Object> map = new HashMap<>();
                        map.put("status", 401);
                        if (e instanceof LockedException) {
                            map.put("msg", "????????????????????????????????????");
                        } else if (e instanceof DisabledException) {
                            map.put("msg", "????????????????????????????????????");
                        } else if (e instanceof CredentialsExpiredException) {
                            map.put("msg", "?????????????????????????????????");
                        } else if (e instanceof AccountExpiredException) {
                            map.put("msg", "?????????????????????????????????");
                        } else if (e instanceof BadCredentialsException) {
                            map.put("msg", "???????????????????????????");
                        } else {
                            map.put("msg", "???????????????????????????");
                        }
                        out.write(new ObjectMapper().writeValueAsString(map));
                        out.flush();
                        out.close();
                    }
                })
                //????????????????????????????????????
                .permitAll()
                .and()
                //????????????
                .logout()
                .logoutUrl("/logout")
                //????????????????????????
                .logoutSuccessHandler(new LogoutSuccessHandler() {
                    @Override
                    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                                Authentication auth) throws IOException, ServletException {

                        response.setContentType("application/json;charset=UTF-8");
                        PrintWriter out = response.getWriter();
                        Map<String, Object> map = new HashMap<>();
                        map.put("status", 200);
                        map.put("msg", "????????????");
                        out.write(new ObjectMapper().writeValueAsString(map));
                        out.flush();
                        out.close();

                    }
                })
                .permitAll()
                .and()
                //??????csrf???????????????????????????????????????????????????
                .csrf()
                .disable();
    }
}
