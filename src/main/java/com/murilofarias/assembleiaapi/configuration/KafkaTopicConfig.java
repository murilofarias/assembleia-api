package com.murilofarias.assembleiaapi.configuration;

import com.murilofarias.assembleiaapi.infra.message.dto.VotoMessageDto;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {

    @Value("${spring.kafka.consumer.bootstrap-servers}")
    private String consumer_BootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String consumer_groupId;

    @Value("${spring.kafka.consumer.auto-offset-reset}")
    private String consumer_autoOffsetReset;

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

    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        JsonDeserializer<Object> deserializer = new JsonDeserializer<>(Object.class);
        deserializer.addTrustedPackages("*");

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, consumer_BootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, consumer_groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer.getClass());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, consumer_autoOffsetReset);
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer );
    }

    @Bean
    KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Object>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(4);
        factory.getContainerProperties().setPollTimeout(3000);
        return factory;
    }
}
