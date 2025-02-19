package com.springprojects.realtimechatapp.config;

import org.springframework.context.annotation.Configuration;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    private final KafkaSSLConfig kafkaSSLConfig;

    public KafkaConsumerConfig(KafkaSSLConfig kafkaSSLConfig) {
        this.kafkaSSLConfig = kafkaSSLConfig;
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaSSLConfig.getBootstrapServers());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "group-" + java.util.UUID.randomUUID().toString());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        if (kafkaSSLConfig.getSecurityProtocol() != null) {
            props.put("security.protocol", kafkaSSLConfig.getSecurityProtocol());
        }
        if (kafkaSSLConfig.getTruststoreLocation() != null) {
            props.put("ssl.truststore.location", kafkaSSLConfig.getTruststoreLocation());
        }
        if (kafkaSSLConfig.getTruststorePassword() != null) {
            props.put("ssl.truststore.password", kafkaSSLConfig.getTruststorePassword());
        }
        if (kafkaSSLConfig.getKeystoreLocation() != null) {
            props.put("ssl.keystore.location", kafkaSSLConfig.getKeystoreLocation());
        }
        if (kafkaSSLConfig.getKeystorePassword() != null) {
            props.put("ssl.keystore.password", kafkaSSLConfig.getKeystorePassword());
        }
        if (kafkaSSLConfig.getKeyPassword() != null) {
            props.put("ssl.key.password", kafkaSSLConfig.getKeyPassword());
        }

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
            ConsumerFactory<String, String> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }
}
