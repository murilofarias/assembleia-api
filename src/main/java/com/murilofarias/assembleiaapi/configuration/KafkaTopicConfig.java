package com.murilofarias.assembleiaapi.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {


    @Value("${kafka.topic.resultado-sessao.num-partitions: 1}")
    private Short topicResultadoSessaoNumPartitions;

    @Value("${kafka.topic.resultado-sessao.replicas: 1}")
    private Short topicResultadoSessaoReplicas;

    @Value("${kafka.topic.votos.num-partitions: 1}")
    private Short topicVotosNumPartitions;

    @Value("${kafka.topic.votos.replicas: 1}")
    private Short topicVotosReplicas;

    @Bean
    public NewTopic topic2() {
        return new NewTopic("resultadoSessao", topicResultadoSessaoNumPartitions, topicResultadoSessaoReplicas);
    }

    @Bean
    public NewTopic topic1() {
        return new NewTopic("votos", topicVotosNumPartitions, topicVotosReplicas);
    }
}
