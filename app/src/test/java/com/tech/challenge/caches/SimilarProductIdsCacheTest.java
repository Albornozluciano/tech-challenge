package com.tech.challenge.caches;

import com.github.benmanes.caffeine.cache.Cache;
import com.tech.challenge.TestHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@WebFluxTest(SimilarProductIdsCache.class)
public class SimilarProductIdsCacheTest extends TestHelper {
    @MockBean
    Cache<String, SimilarProductIdsCache.CacheObject> cache;

    @Autowired
    SimilarProductIdsCache similarProductIdsCache;

    @Test
    void getCachedInfoShouldReturnIt() {
        Integer[] value = new Integer[1];
        value[0] = SIMILAR_PRODUCT_ID;

        when(cache.getIfPresent(SPI_PREFIX_KEY + PRODUCT_ID)).thenReturn(new SimilarProductIdsCache.CacheObject(value));

        Flux<Integer> info = similarProductIdsCache.getInfo(PRODUCT_ID);
        assertEquals(SIMILAR_PRODUCT_ID, info.blockFirst());
    }

    @Test
    void getNullCachedInfoShouldReturnNullValue() {
        when(cache.getIfPresent(SPI_PREFIX_KEY + PRODUCT_ID)).thenReturn(null);

        Flux<Integer> info = similarProductIdsCache.getInfo(PRODUCT_ID);

        assertNull(info);
    }

    @Test
    void getEmptyCachedInfoShouldReturnNullValue() {
        Integer[] value = new Integer[0];

        when(cache.getIfPresent(SPI_PREFIX_KEY + PRODUCT_ID)).thenReturn(new SimilarProductIdsCache.CacheObject(value));

        Flux<Integer> info = similarProductIdsCache.getInfo(PRODUCT_ID);

        assertNull(info);
    }

    @Test
    void getCachedInfoWithNullValueShouldReturnNullValue() {
        when(cache.getIfPresent(SPI_PREFIX_KEY + PRODUCT_ID)).thenReturn(new SimilarProductIdsCache.CacheObject(null));

        Flux<Integer> info = similarProductIdsCache.getInfo(PRODUCT_ID);

        assertNull(info);
    }

    @Test
    void saveInfoWhenInfoIsNotCachedShouldSaveInfoCorrectly() {
        Integer[] value = new Integer[1];
        value[0] = SIMILAR_PRODUCT_ID;

        when(cache.getIfPresent(SPI_PREFIX_KEY + PRODUCT_ID)).thenReturn(null);

        Flux<Integer> info = similarProductIdsCache.saveInfo(value, PRODUCT_ID);

        assertNotNull(info);
        assertEquals(value[0], info.blockFirst());
    }

    @Test
    void saveInfoWhenInfoIsCachedShouldReturnCachedInfo() {
        Integer[] value = new Integer[1];
        value[0] = SIMILAR_PRODUCT_ID;

        when(cache.getIfPresent(SPI_PREFIX_KEY + PRODUCT_ID)).thenReturn(new SimilarProductIdsCache.CacheObject(value));

        Flux<Integer> info = similarProductIdsCache.saveInfo(null, PRODUCT_ID);

        assertEquals(SIMILAR_PRODUCT_ID, info.blockFirst());
    }

}
