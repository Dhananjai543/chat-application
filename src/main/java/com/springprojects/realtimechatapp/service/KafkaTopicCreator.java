package com.springprojects.realtimechatapp.service;

import com.springprojects.realtimechatapp.config.KafkaSSLConfig;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

@Service
public class KafkaTopicCreator {

    @Autowired
    private KafkaSSLConfig kafkaSSLConfig;

    @Async
    public CompletableFuture<Void> createTopicIfNotExist(String topicName) {
        return CompletableFuture.runAsync(() -> {
            Properties config = kafkaSSLConfig.getSimpleKafkaProperties();

            try (AdminClient admin = AdminClient.create(config)) {
                if (admin.listTopics().names().get().stream().noneMatch(topicName::equals)) {
                    System.out.println("Creating topic: " + topicName);
                    admin.createTopics(Collections.singletonList(new NewTopic(topicName, 1, (short) 1)));
                } else {
                    System.out.println("Topic exists: " + topicName);
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to create topic", e);
            }
        });
    }
}