package com.springprojects.realtimechatapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springprojects.realtimechatapp.entity.ChatMessage;
import com.springprojects.realtimechatapp.service.KafkaConsumerService;
import com.springprojects.realtimechatapp.service.KafkaTopicCreator;
import com.springprojects.realtimechatapp.service.RedisService;
import com.springprojects.realtimechatapp.utilities.CipherHelper;
import com.springprojects.realtimechatapp.utilities.MessageTracker;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ChatController {
	
	private final ObjectMapper obj = new ObjectMapper();

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    
    @Autowired
    private KafkaTopicCreator kafkaTopicCreator;

    @Autowired
    private KafkaConsumerService kafkaConsumerService;
    
    @Autowired
    private RedisService redisService;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessage chatMessage){
        //return chatMessage;
        String chatGroupName = chatMessage.getChatGroupName();
        String jsonMessage = "";
        try {
			jsonMessage = obj.writeValueAsString(chatMessage);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
        String encryptedMessage = CipherHelper.encrypt(jsonMessage);
        
        kafkaTopicCreator.createTopicIfNotExist(chatGroupName)
        .exceptionally(ex -> {
            // handle exception here
            System.err.println("Failed to create topic: " + ex.getMessage());
            return null;
        }).thenRun(() -> {
            kafkaTemplate.send(chatGroupName, encryptedMessage);
        });

        simpMessagingTemplate.convertAndSend("/topic/" + chatGroupName, chatMessage);
    }

    @MessageMapping("/chat.addUser")
    //@SendTo("/topic/public")
    public void addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor){
        
    	//clear previous messages loaded from map for currently logged in user
    	MessageTracker.clearMessages(chatMessage.getSender());
    	
    	String username = chatMessage.getSender();
    	boolean hasMessages = redisService.hasKeyLike(username+"&"+chatMessage.getChatGroupName());
    	if(!hasMessages) {
    		log.info("No messages present in redis for username [" +username+ "]. Adding listener!");
    		kafkaConsumerService.addListener(chatMessage.getChatGroupName(), chatMessage.getSender());
    	}else {
    		log.info("Messages present in redis for username [" +username+ "]. Skip adding listener!");
    	}
        
    	//add username in websocket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());

        simpMessagingTemplate.convertAndSend("/topic/" + chatMessage.getChatGroupName(), chatMessage);

    }

    @GetMapping("/messages")
    public ResponseEntity<List<String>> getMessages(@RequestParam String username, @RequestParam String gname) {
    	System.out.println("Debug 1");
//        List<ChatMessage> messages = kafkaConsumerService.getMessages();
//        for(ChatMessage m : messages){
//            System.out.println("Fetched by api: " + m.toString());
//        }
    	List<String> messages = redisService.getMessagesByKeyword(username + "&" + gname);
    	if(messages.size()==0) {
    		List<String> messagesFromKafka = kafkaConsumerService.getMessages();
    		return ResponseEntity.ok(messagesFromKafka);
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
