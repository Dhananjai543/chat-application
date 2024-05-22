package com.springprojects.realtimechatapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class ChatUserController {

    @GetMapping("/hello")
    public String showLoginForm(Model theModel){
        return "login-form";
    }

}
