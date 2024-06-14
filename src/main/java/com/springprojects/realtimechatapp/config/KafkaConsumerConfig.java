package com.springprojects.realtimechatapp.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

//    @Autowired
//    private ConsumerFactory<String, String> consumerFactory;
//
//    public ConcurrentKafkaListenerContainerFactory<String, String> concurrentKafkaListenerContainerFactory()
//    {
//        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(consumerFactory);
//        return factory;
//    }

//    public ConcurrentKafkaListenerContainerFactory<String, String> concurrentKafkaListenerContainerFactory()
//    {
//        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(consumerFactory);
//        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
//        factory.getContainerProperties().setSyncCommits(true);
//        Map<String, Object> props = new HashMap<>(consumerFactory.getConfigurationProperties());
//        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
//        factory.getConsumerFactory().getConfigurationProperties().putAll(props);
//        return factory;
//    }

}
