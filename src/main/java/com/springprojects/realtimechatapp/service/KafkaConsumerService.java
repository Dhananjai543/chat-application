package com.springprojects.realtimechatapp.service;

import com.springprojects.realtimechatapp.entity.ChatMessage;
import com.springprojects.realtimechatapp.entity.MessageType;
import com.springprojects.realtimechatapp.utilities.MessageTracker;

import lombok.Getter;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class KafkaConsumerService {

    @Autowired
    private ConcurrentKafkaListenerContainerFactory<String, String> factory;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    private Map<String, ConcurrentMessageListenerContainer<String, String>> listenerContainers = new HashMap<>();

    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Getter
    private List<ChatMessage> messages;


    public void addListener(String chatGroupName, String username) {
    	
    	messages =  new ArrayList<>();

        System.out.println("Adding listener to kafka: " + chatGroupName);

        Map<String, Object> props = new HashMap<>(factory.getConsumerFactory().getConfigurationProperties());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "group-" + chatGroupName + "-" + UUID.randomUUID().toString());

        DefaultKafkaConsumerFactory<String, String> consumerFactory = new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new StringDeserializer());
        factory.setConsumerFactory(consumerFactory);

        ConcurrentMessageListenerContainer<String, String> container = factory.createContainer(chatGroupName);
        container.setupMessageListener((MessageListener<String, String>) message -> {

            System.out.println("Fetched message: "+ message);
            try {
                Pattern pattern = Pattern.compile("ChatMessage\\(content=(.*), sender=(.*), messageType=(.*), chatGroupName=(.*)\\)");
                Matcher matcher = pattern.matcher(message.value());

                if (matcher.find()) {
                    String content = matcher.group(1);
                    String sender = matcher.group(2);
                    MessageType messageType = MessageType.valueOf(matcher.group(3));
                    String currChatGroupName = matcher.group(4);

                    ChatMessage chatMessage = new ChatMessage(content, sender, messageType, currChatGroupName);

                    String key = username + message.offset() + message.partition();
                    if(!MessageTracker.checkMessageIfAlreadyConsumed(username, key)){
                        System.out.println("Message key [" + key + "] not yet consumed for user [" + username + "]");
                        messages.add(chatMessage);
                        System.out.println("Consumed message: " + message);
                    }else{
                        System.out.println("Message key [" + key + "] already consumed for user [" + username + "]");
                    }

                } else {
                    System.err.println("Failed to parse message: " + message.value());
                }

            } catch (Exception e) {
                System.err.println("Failed to deserialize message: " + e.getMessage());
            }
        });

        container.start();
        // Keep track of the created containers to be able to stop them later
        listenerContainers.put(chatGroupName, container);

        scheduler.schedule(() -> removeListener(chatGroupName), 20, TimeUnit.SECONDS);

    }

    public void removeListener(String chatGroupName) {
        ConcurrentMessageListenerContainer<String, String> container = listenerContainers.get(chatGroupName);
        if (container != null) {
            container.stop();
            listenerContainers.remove(chatGroupName);
        }
    }

}
