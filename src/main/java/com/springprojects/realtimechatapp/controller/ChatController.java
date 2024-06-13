package com.springprojects.realtimechatapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.springprojects.realtimechatapp.entity.ChatMessage;
import com.springprojects.realtimechatapp.service.KafkaTopicCreator;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    
    @Autowired
    private KafkaTopicCreator kafkaTopicCreator;


    @MessageMapping("/chat.sendMessage")
	//@SendTo("/topic/public")
    public void sendMessage(@Payload ChatMessage chatMessage){
    	//return chatMessage;
        kafkaTopicCreator.createTopicIfNotExist(chatMessage.getChatGroupName())
                .exceptionally(ex -> {
                    // handle exception here
                    System.err.println("Failed to create topic: " + ex.getMessage());
                    return null;
                }).thenRun(() -> {
                    kafkaTemplate.send(chatMessage.getChatGroupName(), chatMessage.toString());
                });;
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

//    @PostMapping("/api/sendMessageToKafka")
//    public void sendMessage(@RequestBody String message) {
//        messageProducer.sendMessage("your-topic", message);
//    }
}
