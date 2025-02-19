package com.springprojects.realtimechatapp.service;

import com.springprojects.realtimechatapp.config.KafkaSSLConfig;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
            Properties config = new Properties();
            config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaSSLConfig.getBootstrapServers());

            if (kafkaSSLConfig.getSaslMechanism() != null) {
                config.put("sasl.mechanism", kafkaSSLConfig.getSaslMechanism());
            }

            if (kafkaSSLConfig.getSecurityProtocol() != null) {
                config.put("security.protocol", kafkaSSLConfig.getSecurityProtocol());
            }

            if (kafkaSSLConfig.getJaasConfig() != null) {
                config.put("sasl.jaas.config", kafkaSSLConfig.getJaasConfig());
            }

            config.put("ssl.truststore.location", kafkaSSLConfig.getTruststoreLocation());
            config.put("ssl.truststore.password", kafkaSSLConfig.getTruststorePassword());
            config.put("ssl.keystore.location", kafkaSSLConfig.getKeystoreLocation());
            config.put("ssl.keystore.password", kafkaSSLConfig.getKeystorePassword());
            config.put("ssl.key.password", kafkaSSLConfig.getKeyPassword());

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