package com.heanzyzabala.idempotencykey;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@EnableConfigurationProperties({ IdempotencyKeyFilterProperties.class })
public class IdempotencyKeyAutoConfiguration {

    @Autowired
    private IdempotencyKeyFilterProperties properties;

    @Bean
    public InMemoryIdempotencyKeyStore inMemoryIdempotencyKeyStore() {
        return new InMemoryIdempotencyKeyStore();
    }

    @Bean
    public IdempotencyKeyFilter idempotencyKeyFilter(IdempotencyKeyStore idempotencyKeyStore) {
        return new IdempotencyKeyFilter(properties, matcher, idempotencyKeyStore);
    }
}