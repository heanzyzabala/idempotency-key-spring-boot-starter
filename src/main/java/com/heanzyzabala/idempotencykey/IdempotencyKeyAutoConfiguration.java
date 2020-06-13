package com.heanzyzabala.idempotencykey;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;

@Slf4j
@Configuration
@EnableConfigurationProperties({ IdempotencyKeyFilterProperties.class })
public class IdempotencyKeyAutoConfiguration {

    @Autowired
    private IdempotencyKeyFilterProperties properties;

    @Bean
    public ConcurrentHashMapKeyStore concurrentHashMapKeyStore() {
        return new ConcurrentHashMapKeyStore();
    }

    @Bean
    public IdempotencyKeyFilter idempotencyKeyFilter(IdempotencyKeyStore idempotencyKeyStore) {
        log.info("Configured idempotency key: {} and required paths: {}", properties.getHeaderName(), properties.getRequiredPaths());
        return new IdempotencyKeyFilter(properties, new AntPathMatcher(), idempotencyKeyStore);
    }
}
