package com.springprojects.realtimechatapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.springprojects.realtimechatapp.entity.ChatUser;
import com.springprojects.realtimechatapp.service.ChatUserService;

@Controller
@RequestMapping("/")
public class ChatUserController {

	@Autowired
	private ChatUserService chatUserService;
	
    @GetMapping("/showLoginForm")
    public String showLoginForm(Model theModel){
        return "login-form";
    }
    
    @GetMapping("/showSignUpForm")
    public String showSignUpForm(Model theModel) {
    	theModel.addAttribute("chatUser", new ChatUser());
    	return "sign-up-form";
    }
    
    @PostMapping("/processSignUpForm")
    public String processSignUpForm(@ModelAttribute("chatUser") ChatUser chatUser) {
    	
    	System.out.println(chatUser.toString());
    	chatUserService.saveChatUser(chatUser);
    	return "chat-page";
    }

}
