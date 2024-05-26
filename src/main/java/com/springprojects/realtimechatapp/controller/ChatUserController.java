package com.springprojects.realtimechatapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.springprojects.realtimechatapp.entity.ChatUser;
import com.springprojects.realtimechatapp.service.ChatUserService;

import jakarta.validation.Valid;

@Controller
public class ChatUserController {

	@Autowired
	private ChatUserService chatUserService;
	
    @GetMapping("/showLoginForm")
    public String showLoginForm(){
        return "login-form";
    }
    
    @GetMapping("/showSignUpForm")
    public String showSignUpForm(Model theModel) {
    	theModel.addAttribute("chatUser", new ChatUser());
    	return "sign-up-form";
    }
    
    
    
    @PostMapping("/processSignUpForm")
    public String processSignUpForm(@Valid @ModelAttribute("chatUser") ChatUser chatUser, BindingResult bindingResult, Model model) {
    	
    	
    	//validation errors from ChatUser Entity
        if (bindingResult.hasErrors()) {
        	for (FieldError error : bindingResult.getFieldErrors()) {
                model.addAttribute("error", error.getDefaultMessage());
            }
            return "sign-up-form";
        }
    	
        try {
            System.out.println(chatUser.toString());
            chatUserService.saveChatUser(chatUser);
            return "chat-page";
        } catch (Exception e) {
            model.addAttribute("error", "There already exists an account with this email");
            return "sign-up-form";
        }
    }
    
    @GetMapping("/showChatPage")
    public String showChatPage() {
    	return "chat-page";
    }

}
