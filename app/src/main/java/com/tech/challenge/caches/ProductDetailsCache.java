package com.tech.challenge.caches;

import com.github.benmanes.caffeine.cache.Cache;
import com.tech.challenge.dtos.SimilarProductsDetailed;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
public class ProductDetailsCache {
    private final String PREFIX_KEY = "PD#";
    private final Cache<String, CacheObject> cache;

    @Autowired
    public ProductDetailsCache(Cache<String, CacheObject> cache) {
        this.cache = cache;
    }

    public Mono<SimilarProductsDetailed> getInfo(String productId) {
        CacheObject cachedObject = cache.getIfPresent(PREFIX_KEY + productId);
        if (cachedObject != null) {
            if (cachedObject.value != null) {
                log.debug("Retrieving product " + productId + " details from cache. Response data: " + cachedObject.value);
                return Mono.just(cachedObject.value);
            } else {
                log.debug("Retrieving product " + productId + " details from cache. Empty response data");
                return Mono.empty();
            }
        }
        log.debug("Not found " + productId + " details in cache.");
        return null;
    }

    public Mono<SimilarProductsDetailed> saveInfo(SimilarProductsDetailed info, String productId) {
        Mono<SimilarProductsDetailed> cachedSimilarProductsDetailed = getInfo(productId);
        if (cachedSimilarProductsDetailed != null) {
            return cachedSimilarProductsDetailed;
        }
        log.debug("Saving product " + productId + " detail to cache. Response data: " + info);
        cache.put(PREFIX_KEY + productId, new CacheObject(info));
        return Mono.justOrEmpty(info);
    }

    @AllArgsConstructor
    public static class CacheObject {
        SimilarProductsDetailed value;
    }
}