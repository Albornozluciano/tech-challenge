package com.tech.challenge.services;

import com.tech.challenge.TestHelper;
import com.tech.challenge.caches.ProductDetailsCache;
import com.tech.challenge.clients.rest.ProductsDetailAPIClient;
import com.tech.challenge.dtos.SimilarProductsDetailed;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.util.concurrent.TimeoutException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@WebFluxTest(ProductDetailService.class)
public class ProductDetailServiceTest extends TestHelper {
    @MockBean
    ProductsDetailAPIClient productsDetailAPIClient;
    @MockBean
    ProductDetailsCache cache;
    @Autowired
    ProductDetailService productDetailService;

    @Test
    void getDetailsByIdWhenInfoIsNotCachedShouldReturnInfoFromAPI() {
        when(productsDetailAPIClient.getDetailsById(PRODUCT_ID)).thenReturn(Mono.just(SIMILAR_PRODUCT_DETAILED));
        when(cache.getInfo(PRODUCT_ID)).thenReturn(null);

        Mono<SimilarProductsDetailed> result = productDetailService.getDetailsById(PRODUCT_ID);
        assertEquals(SIMILAR_PRODUCT_DETAILED, result.block());
    }

    @Test
    void getDetailsByIdWhenInfoIsCachedShouldReturnInfoFromCache() {
        when(productsDetailAPIClient.getDetailsById(PRODUCT_ID)).thenReturn(Mono.empty());
        when(cache.getInfo(PRODUCT_ID)).thenReturn(Mono.just(SIMILAR_PRODUCT_DETAILED));

        Mono<SimilarProductsDetailed> result = productDetailService.getDetailsById(PRODUCT_ID);
        assertEquals(SIMILAR_PRODUCT_DETAILED, result.block());
    }

    @Test
    void getDetailsByIdWhenTimeoutExceptionIsThrownShouldReturnEmptyInfo() {
        when(productsDetailAPIClient.getDetailsById(PRODUCT_ID))
                .thenReturn(
                        Mono.error(
                                new TimeoutException("timeout")
                        )
                );
        when(cache.getInfo(PRODUCT_ID)).thenReturn(null);

        StepVerifier.create(productDetailService.getDetailsById(PRODUCT_ID)).expectNextCount(0).verifyComplete();
    }

    @Test
    void getDetailsByIdWhenNotFoundExceptionIsThrownShouldReturnEmptyInfo() {
        when(productsDetailAPIClient.getDetailsById(PRODUCT_ID))
                .thenReturn(
                        Mono.error(
                                new WebClientResponseException(HttpStatus.NOT_FOUND.value(), "Not found", null, null, null)
                        )
                );

        when(cache.getInfo(PRODUCT_ID)).thenReturn(null);

        StepVerifier.create(productDetailService.getDetailsById(PRODUCT_ID)).expectNextCount(0).verifyComplete();
    }

    @Test
    void getDetailsByIdWhenInternalServerErrorExceptionIsThrownShouldReturnEmptyInfo() {
        when(productsDetailAPIClient.getDetailsById(PRODUCT_ID))
            .thenReturn(
                Mono.error(
                    new WebClientResponseException(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error!", null, null, null
                    )
                )
            );

        when(cache.getInfo(PRODUCT_ID)).thenReturn(null);

        StepVerifier.create(productDetailService.getDetailsById(PRODUCT_ID)).expectNextCount(0).verifyComplete();
    }
}
