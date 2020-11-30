package com.ouva.demo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ouva.demo.dto.HeartbeatKafkaRecord;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

@EnableKafka
@Configuration
public class HeartbeatConsumerConfig {

  @Value("${spring.kafka.consumer.topic}")
  private String topic;

  @Value("${spring.kafka.listener.concurrency:3}")
  private Integer concurrency;

  @Value("${spring.kafka.consumer.partition:3}")
  private Integer partition;

  /**
   * The ConsumerFactory implementation to produce new Consumer instances for provided Map configs and optional
   * Deserializers on each ConsumerFactory.createConsumer() invocation.
   *
   * @param kafkaProperties properties collected from application.yml.
   * @return a ConsumerFactory implementation.
   */
  @Bean
  public ConsumerFactory<String, HeartbeatKafkaRecord> consumerFactory(KafkaProperties kafkaProperties) {
    return new DefaultKafkaConsumerFactory<>(kafkaProperties.buildConsumerProperties());
  }

  @Bean
  public NewTopic compactTopicExample() {
    return TopicBuilder.name(topic)
      .partitions(partition)
      .compact()
      .build();
  }

  @Bean
  public ObjectMapper objectMapper() {
    return JsonMapper.builder()
      // To recognize JSON local time properties
      .addModule(new JavaTimeModule())
      .build();
  }

}
