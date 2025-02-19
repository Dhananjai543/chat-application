package com.springprojects.realtimechatapp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springprojects.realtimechatapp.entity.ChatMessage;
import com.springprojects.realtimechatapp.utilities.CipherHelper;
import com.springprojects.realtimechatapp.utilities.MessageTracker;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class KafkaConsumerService {

	private final ObjectMapper obj = new ObjectMapper();

	@Autowired
	private ConcurrentKafkaListenerContainerFactory<String, String> factory;

	private Map<String, ConcurrentMessageListenerContainer<String, String>> listenerContainers = new HashMap<>();

	private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	@Getter
	private List<String> messages;

	public void addListener(String chatGroupName, String username) {

		messages = new ArrayList<>();

		System.out.println("Adding listener to kafka: " + chatGroupName);

		ConcurrentMessageListenerContainer<String, String> container = factory.createContainer(chatGroupName);
		container.setupMessageListener((MessageListener<String, String>) message -> {

			System.out.println("Fetched message: " + message);
			String messageValue = message.value();
			String decryptedMessageString = CipherHelper.decrypt(messageValue);
			System.out.println("Decrypted Json message: " + decryptedMessageString);

			try {
				ChatMessage chatMessage = obj.readValue(decryptedMessageString, ChatMessage.class);
				String key = username + "&" + chatGroupName + "-" + message.offset();
				if (!MessageTracker.checkMessageIfAlreadyConsumed(username, key)) {
					System.out.println("Message key [" + key + "] not yet consumed for user [" + username + "]");
					messages.add(obj.writeValueAsString(chatMessage));
//					redisService.pushToZSet(key, chatMessage.toString(), Integer.parseInt(redisMessagesTimeoutMinutes));
					System.out.println("Consumed message: " + message);
				} else {
					System.out.println("Message ksey [" + key + "] already consumed for user [" + username + "]");
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