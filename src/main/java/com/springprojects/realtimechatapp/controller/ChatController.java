package com.springprojects.realtimechatapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${redis.message.timeout.minutes:#{1}}")
    private String redisMessagesTimeoutMinutes;

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

        String finalJsonMessage = jsonMessage;
        kafkaTemplate.send(chatGroupName, encryptedMessage);
        redisService.pushToZSet(chatMessage.getSender()+"&"+chatGroupName, finalJsonMessage, Integer.parseInt(redisMessagesTimeoutMinutes));

        simpMessagingTemplate.convertAndSend("/topic/" + chatGroupName, chatMessage);
    }

    @MessageMapping("/chat.addUser")
    //@SendTo("/topic/public")
    public void addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor){
        
    	//clear previous messages loaded from map for currently logged in user
    	MessageTracker.clearMessages(chatMessage.getSender());

    	String username = chatMessage.getSender();

        //clear expired messages from redis
        redisService.removeExpiredMessages(username+"&"+chatMessage.getChatGroupName());

    	boolean hasMessages = redisService.hasKey(username+"&"+chatMessage.getChatGroupName());
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
        List<String> messages = redisService.getNonExpiredMessages(username + "&" + gname);
        System.out.println("Messages size [redis]: " + messages.size());
        if(messages.size()==0) {
    		List<String> messagesFromKafka = kafkaConsumerService.getMessages();
            redisService.pushAllToZSet(username + "&" + gname, messagesFromKafka, Integer.parseInt(redisMessagesTimeoutMinutes));
    		return ResponseEntity.ok(messagesFromKafka);
    	}
        return ResponseEntity.ok(messages);
    }

}
