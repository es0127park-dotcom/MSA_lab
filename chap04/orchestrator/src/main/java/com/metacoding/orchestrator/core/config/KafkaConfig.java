package com.metacoding.orchestrator.core.config;

import org.springframework.context.annotation.*;
import org.springframework.kafka.support.converter.*;

@Configuration
public class KafkaConfig {

    @Bean
    public RecordMessageConverter recordMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
