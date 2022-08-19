package com.tech.challenge.services;

import com.tech.challenge.TestHelper;
import com.tech.challenge.caches.SimilarProductIdsCache;
import com.tech.challenge.clients.rest.ProductsAPIClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.util.concurrent.TimeoutException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@WebFluxTest(SimilarProductIdsService.class)
public class SimilarProductIdsServiceTest extends TestHelper {
    @MockBean
    ProductsAPIClient productsAPIClient;
    @MockBean
    SimilarProductIdsCache cache;
    @Autowired
    SimilarProductIdsService similarProductIdsService;

    @Test
    void getSimilarIdsWhenInfoIsNotCachedShouldReturnInfoFromAPI() {
        Integer[] similarIds = new Integer[1];
        similarIds[0] = SIMILAR_PRODUCT_ID;

        when(productsAPIClient.getSimilarProductIdsByProductId(PRODUCT_ID)).thenReturn(Mono.just(similarIds));
        when(cache.getInfo(PRODUCT_ID)).thenReturn(null);

        Flux<Integer> result = similarProductIdsService.getSimilarProductIdsByProductId(PRODUCT_ID);
        assertEquals(SIMILAR_PRODUCT_ID, result.blockFirst());
    }

    @Test
    void getSimilarIdsWhenInfoIsCachedShouldReturnInfoFromCache() {
        when(productsAPIClient.getSimilarProductIdsByProductId(PRODUCT_ID)).thenReturn(Mono.empty());
        when(cache.getInfo(PRODUCT_ID)).thenReturn(Flux.just(SIMILAR_PRODUCT_ID));

        Flux<Integer> result = similarProductIdsService.getSimilarProductIdsByProductId(PRODUCT_ID);
        assertEquals(SIMILAR_PRODUCT_ID, result.blockFirst());
    }

    @Test
    void getSimilarIdsWhenTimeoutExceptionIsThrownShouldReturnError() {
        when(productsAPIClient.getSimilarProductIdsByProductId(PRODUCT_ID)).thenReturn(Mono.error(TimeoutException::new));
        when(cache.getInfo(PRODUCT_ID)).thenReturn(null);

        StepVerifier.create(similarProductIdsService.getSimilarProductIdsByProductId(PRODUCT_ID)).verifyError();
    }

    @Test
    void getSimilarIdsWhenNotFoundExceptionIsThrownShouldReturnError() {
        when(productsAPIClient.getSimilarProductIdsByProductId(PRODUCT_ID))
            .thenReturn(
                Mono.error(
                    new WebClientResponseException(HttpStatus.NOT_FOUND.value(), "Not found", null, null, null)
                )
            );

        when(cache.getInfo(PRODUCT_ID)).thenReturn(null);

        StepVerifier.create(similarProductIdsService.getSimilarProductIdsByProductId(PRODUCT_ID)).verifyError();
    }


    @Test
    void getSimilarIdsWhenInternalServerErrorIsThrownShouldReturnError() {
        when(productsAPIClient.getSimilarProductIdsByProductId(PRODUCT_ID))
            .thenReturn(
                Mono.error(
                    new WebClientResponseException(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", null, null, null
                    )
                )
            );

        when(cache.getInfo(PRODUCT_ID)).thenReturn(null);

        StepVerifier.create(similarProductIdsService.getSimilarProductIdsByProductId(PRODUCT_ID)).verifyError();
    }
}
