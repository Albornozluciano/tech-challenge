package com.tech.challenge.caches;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import java.util.Arrays;

@Slf4j
@Configuration
public class SimilarProductIdsCache {
    private final String PREFIX_KEY = "SPI#";
    private final Cache<String, CacheObject> cache;

    @Autowired
    public SimilarProductIdsCache(Cache<String, CacheObject> cache) {
        this.cache = cache;
    }

    public Flux<Integer> getInfo(String productId) {
        CacheObject cachedInfo = cache.getIfPresent(PREFIX_KEY + productId);
        if (cachedInfo != null && cachedInfo.value != null && cachedInfo.value.length != 0 ) {
            log.debug("Retrieving product " + productId + " similar ids from cache. Response data: " +
                    Arrays.toString(cachedInfo.value));
            return Flux.fromArray(cachedInfo.value);
        }
        log.debug("Not found " + productId + " similar ids in cache.");
        return null;
    }

    public Flux<Integer> saveInfo(Integer[] info, String productId) {
        Flux<Integer> cachedInfo = getInfo(productId);
        if (getInfo(productId) != null) {
            return cachedInfo;
        }
        log.debug("Saving product " + productId + " similar ids to cache. Response data: " + Arrays.toString(info));
        cache.put(PREFIX_KEY + productId, new CacheObject(info));
        return Flux.just(info);
    }

    @AllArgsConstructor
    public static class CacheObject {
        Integer[] value;
    }
}
