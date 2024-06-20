package com.springprojects.realtimechatapp.controller;

import com.springprojects.realtimechatapp.service.KafkaConsumerService;
import com.springprojects.realtimechatapp.utilities.CipherHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.springprojects.realtimechatapp.entity.ChatMessage;
import com.springprojects.realtimechatapp.service.KafkaTopicCreator;
import com.springprojects.realtimechatapp.utilities.MessageTracker;

import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    
    @Autowired
    private KafkaTopicCreator kafkaTopicCreator;

    @Autowired
    private KafkaConsumerService kafkaConsumerService;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessage chatMessage){
        //return chatMessage;
        String chatGroupName = chatMessage.getChatGroupName();
        String encryptedMessage = CipherHelper.encrypt(chatMessage.toString());

        kafkaTopicCreator.createTopicIfNotExist(chatGroupName)
                .exceptionally(ex -> {
                    // handle exception here
                    System.err.println("Failed to create topic: " + ex.getMessage());
                    return null;
                }).thenRun(() -> {
                    kafkaTemplate.send(chatGroupName, encryptedMessage);
                });;
        simpMessagingTemplate.convertAndSend("/topic/" + chatGroupName, chatMessage);
    }

    @MessageMapping("/chat.addUser")
    //@SendTo("/topic/public")
    public void addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor){
        
    	//clear previous messages loaded from map for currently logged in user
    	MessageTracker.clearMessages(chatMessage.getSender());
    	
        kafkaConsumerService.addListener(chatMessage.getChatGroupName(), chatMessage.getSender());
        
    	//add username in websocket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());

        simpMessagingTemplate.convertAndSend("/topic/" + chatMessage.getChatGroupName(), chatMessage);

    }

    @GetMapping("/messages")
    public ResponseEntity<List<ChatMessage>> getMessages() {
    	System.out.println("Debug 1");
        List<ChatMessage> messages = kafkaConsumerService.getMessages();
        for(ChatMessage m : messages){
            System.out.println("Fetched by api: " + m.toString());
        }
        return ResponseEntity.ok(messages);
    }



//    @KafkaListener(topics = "friends",groupId = "group_id")
//    public void consume(String message)
//    {
//        System.out.println("message = " + message);
//    }

//    @PostMapping("/api/sendMessageToKafka")
//    public void sendMessage(@RequestBody String message) {
//        messageProducer.sendMessage("your-topic", message);
//    }
}
