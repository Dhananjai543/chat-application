package com.springprojects.realtimechatapp.service;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

@Service
public class KafkaTopicCreator {

    @Value("${spring.kafka.bootstrap-servers:#{null}}")
    private String bootstrapServers;

    @Value("${spring.kafka.properties.sasl.jaas.config:#{null}}")
    private String jaasConfig;

    @Value("${spring.kafka.properties.security.protocol:#{null}}")
    private String securityProtocol;

    @Value("${spring.kafka.properties.sasl.mechanism:#{null}}")
    private String saslMechanism;

    @Async
    public CompletableFuture<Void> createTopicIfNotExist(String topicName) {
        return CompletableFuture.runAsync(() -> {
            Properties config = new Properties();
            config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
            if(saslMechanism!=null) {
            	config.put("sasl.mechanism", saslMechanism);
            }
            
            if(securityProtocol!=null) {
            	config.put("security.protocol", securityProtocol);
            }
            
            if(jaasConfig!=null) {
            	config.put("sasl.jaas.config", jaasConfig);
            }
            
            try (AdminClient admin = AdminClient.create(config)) {
                if (admin.listTopics().names().get().stream().noneMatch(topicName::equals)) {
                    System.out.println("Creating topic: " + topicName);
                    admin.createTopics(Collections.singletonList(new NewTopic(topicName, 1, (short) 1)));
                }else {
                    System.out.println("Topic exists: " + topicName);
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to create topic", e);
            }
        });
    }
}