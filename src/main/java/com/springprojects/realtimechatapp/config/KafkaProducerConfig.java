package com.springprojects.realtimechatapp.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Autowired
    private KafkaSSLConfig kafkaSSLConfig;

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaSSLConfig.getBootstrapServers());
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        if (kafkaSSLConfig.getSecurityProtocol()!=null){
            configProps.put("security.protocol", kafkaSSLConfig.getSecurityProtocol());
        }
        if (kafkaSSLConfig.getTruststoreLocation()!=null){
            configProps.put("ssl.truststore.location", kafkaSSLConfig.getTruststoreLocation());
        }
        if (kafkaSSLConfig.getTruststorePassword()!=null){
            configProps.put("ssl.truststore.password", kafkaSSLConfig.getTruststorePassword());
        }
        if (kafkaSSLConfig.getKeystoreLocation()!=null){
            configProps.put("ssl.keystore.location", kafkaSSLConfig.getKeystoreLocation());
        }
        if (kafkaSSLConfig.getKeystorePassword()!=null){
            configProps.put("ssl.keystore.password", kafkaSSLConfig.getKeystorePassword());
        }
        if (kafkaSSLConfig.getKeyPassword()!=null){
            configProps.put("ssl.key.password", kafkaSSLConfig.getKeyPassword());
        }
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}