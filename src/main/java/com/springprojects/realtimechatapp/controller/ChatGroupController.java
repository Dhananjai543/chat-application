package com.springprojects.realtimechatapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.springprojects.realtimechatapp.entity.ChatGroup;
import com.springprojects.realtimechatapp.service.ChatGroupService;
import com.springprojects.realtimechatapp.service.KafkaTopicCreator;

import jakarta.validation.Valid;


@Controller
public class ChatGroupController {
	
	
	@Autowired
	private ChatGroupService chatGroupService;
	
	@Autowired
    private KafkaTopicCreator kafkaTopicCreator;
	
	
	@PostMapping("/findChatGroup")
	public String getChatPage(@RequestParam("chatgroup") String chatgroup, Model model) {
		ChatGroup foundGroup = chatGroupService.findByChatGroupName(chatgroup);
        if (foundGroup != null) {
            model.addAttribute("chatgroup", chatgroup);
            return "message-page";
        } else {
        	model.addAttribute("errorMessage", "Sorry! You entered an invalid group name.");
            return "chat-page";
        }
	}

    @GetMapping("/showCreateGroupForm")
    public String showCreateGroupForm(Model theModel) {
        theModel.addAttribute("chatGroup", new ChatGroup());
        return "create-group-form";
    }

    @PostMapping("/processCreateGroupForm")
    public String processCreateGroupForm(@Valid @ModelAttribute("chatGroup") ChatGroup chatGroup, BindingResult bindingResult, Model model){
    	
    	if (bindingResult.hasErrors()) {
			for (FieldError error : bindingResult.getFieldErrors()) {
				System.out.println(error.getDefaultMessage());
				model.addAttribute("error", error.getDefaultMessage());
			}
			return "create-group-form";
		}
    	
    	try {

			ChatGroup existingChatGroup = chatGroupService.findByChatGroupName(chatGroup.getGroup_name());
			if(existingChatGroup != null) {
				model.addAttribute("error", "Oops! Please choose another name for your group");
				return "create-group-form";
			}
    		chatGroupService.saveChatGroup(chatGroup);
			kafkaTopicCreator.createTopicIfNotExist(chatGroup.getGroup_name());

    		model.addAttribute("chatgroup", chatGroup.getGroup_name());
            return "message-page";
    	}catch(Exception e) {
    		model.addAttribute("error", "Error creating chat group");
    		return "create-group-form";
    	}
    	
    }


}
