package com.springprojects.realtimechatapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import com.springprojects.realtimechatapp.entity.ChatMessage;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
	
    @MessageMapping("/chat.sendMessage")
	//@SendTo("/topic/public")
    public void sendMessage(@Payload ChatMessage chatMessage){
    	//return chatMessage;
        simpMessagingTemplate.convertAndSend("/topic/" + chatMessage.getChatGroupName(), chatMessage);
    }

    @MessageMapping("/chat.addUser")
    //@SendTo("/topic/public")
    public void addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor){
        //add username in websocket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
    	//headerAccessor.getSessionAttributes().put("username","Dhananjai");
        simpMessagingTemplate.convertAndSend("/topic/" + chatMessage.getChatGroupName(), chatMessage);
    }
}
