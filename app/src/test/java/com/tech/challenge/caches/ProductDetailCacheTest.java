package com.tech.challenge.caches;

import com.github.benmanes.caffeine.cache.Cache;
import com.tech.challenge.TestHelper;
import com.tech.challenge.dtos.SimilarProductsDetailed;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@WebFluxTest(ProductDetailsCache.class)
public class ProductDetailCacheTest extends TestHelper {
    @MockBean
    Cache<String, ProductDetailsCache.CacheObject> cache;

    @Autowired
    ProductDetailsCache productDetailsCache;

    @Test
    void getCachedInfoShouldReturnIt() {
        when(cache.getIfPresent(PD_PREFIX_KEY + SIMILAR_PRODUCT_ID)).thenReturn(new ProductDetailsCache.CacheObject(SIMILAR_PRODUCT_DETAILED));

        Mono<SimilarProductsDetailed> info = productDetailsCache.getInfo(SIMILAR_PRODUCT_ID.toString());

        assertEquals(SIMILAR_PRODUCT_DETAILED, info.block());
    }

    @Test
    void getNullCachedInfoShouldReturnCacheObjectWithNullValue() {
        when(cache.getIfPresent(PD_PREFIX_KEY + SIMILAR_PRODUCT_ID)).thenReturn(new ProductDetailsCache.CacheObject(null));

        Mono<SimilarProductsDetailed> info = productDetailsCache.getInfo(SIMILAR_PRODUCT_ID.toString());

        assertNull(info.block());
    }

    @Test
    void getInfoWhenItsNotCachedShouldReturnNull() {
        when(cache.getIfPresent(PD_PREFIX_KEY + SIMILAR_PRODUCT_ID)).thenReturn(null);

        Mono<SimilarProductsDetailed> info = productDetailsCache.getInfo(SIMILAR_PRODUCT_ID.toString());

        assertNull(info);
    }

    @Test
    void saveInfoWhenInfoIsNotCachedShouldSaveInfoCorrectly() {
        when(cache.getIfPresent(PD_PREFIX_KEY + SIMILAR_PRODUCT_ID)).thenReturn(null);

        Mono<SimilarProductsDetailed> response = productDetailsCache.saveInfo(SIMILAR_PRODUCT_DETAILED, SIMILAR_PRODUCT_ID.toString());

        assertEquals(SIMILAR_PRODUCT_DETAILED, response.block());
    }

    @Test
    void saveInfoWhenInfoIsCachedShouldReturnCachedInfo() {
        when(cache.getIfPresent(PD_PREFIX_KEY + SIMILAR_PRODUCT_ID)).thenReturn(new ProductDetailsCache.CacheObject(SIMILAR_PRODUCT_DETAILED));

        SimilarProductsDetailed similarProductsDetailedToSave = new SimilarProductsDetailed(SIMILAR_PRODUCT_ID_2.toString(), "Another product", BigDecimal.TEN, false);

        Mono<SimilarProductsDetailed> response = productDetailsCache.saveInfo(similarProductsDetailedToSave, SIMILAR_PRODUCT_ID.toString());

        assertEquals(SIMILAR_PRODUCT_DETAILED, response.block());
    }
}
