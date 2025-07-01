package com.regain.auth_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {
    @Bean
    NewTopic checkSendActiveTopic() {
        return new NewTopic("send-email-active",2,(short) 1);
    }
}
