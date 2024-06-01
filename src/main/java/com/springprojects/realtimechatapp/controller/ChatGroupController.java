package com.springprojects.realtimechatapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.springprojects.realtimechatapp.entity.ChatGroup;
import com.springprojects.realtimechatapp.service.ChatGroupService;


@Controller
public class ChatGroupController {
	
	
	@Autowired
	private ChatGroupService chatGroupService;
	
	
	@PostMapping("/findChatGroup")
	public String getChatPage(@RequestParam("chatgroup") String chatgroup, Model model) {
		// replace "userExists" with the actual condition to check if the user exists in
		// the database
		ChatGroup foundGroup = chatGroupService.findByChatGroupName(chatgroup);
        if (foundGroup != null) {
        	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String currentUsername = auth.getName();
            System.out.println("Current Username: " + currentUsername);
            model.addAttribute("currentUsername", currentUsername);
            return "message-page";
        } else {
        	model.addAttribute("errorMessage", "Sorry! You entered an invalid group name.");
            return "chat-page";
        }
	}
	
}
