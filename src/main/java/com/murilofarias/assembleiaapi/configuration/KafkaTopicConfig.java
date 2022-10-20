package com.murilofarias.assembleiaapi.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {


    @Bean
    public NewTopic topic2() {
        return new NewTopic("resultadoSessao", 1, (short) 1);
    }

    @Bean
    public NewTopic topic1() {
        return new NewTopic("votos", 1, (short) 2);
    }
}
