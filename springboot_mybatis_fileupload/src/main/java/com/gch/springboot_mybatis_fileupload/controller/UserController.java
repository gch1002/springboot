package com.gch.springboot_mybatis_fileupload.controller;

import com.gch.springboot_mybatis_fileupload.domain.User;
import com.gch.springboot_mybatis_fileupload.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("user")
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("login")
    public String login(User user, HttpSession session) {
        User user1 = userService.login(user);
        if (user1 != null) {
            session.setAttribute("user", user1);
            return "redirect:/file/findAll";
        } else {
            return "redirect:/index";
        }
    }
}
