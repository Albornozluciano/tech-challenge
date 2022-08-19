package com.tech.challenge.caches;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.Duration;

@Slf4j
@Configuration
public class GenericCache {
    private final static Integer CACHE_TTL = 300;
    private final static Integer CACHE_SIZE = 10000;

    @Bean
    Cache getCache() {
        return Caffeine.newBuilder()
                .maximumSize(CACHE_SIZE)
                .expireAfterWrite(Duration.ofSeconds(CACHE_TTL))
                .build();
    }
}
