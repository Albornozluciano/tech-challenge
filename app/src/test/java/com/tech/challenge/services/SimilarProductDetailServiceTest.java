package com.tech.challenge.services;

import com.tech.challenge.TestHelper;
import com.tech.challenge.dtos.SimilarProductsDetailed;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@WebFluxTest(SimilarProductDetailService.class)
public class SimilarProductDetailServiceTest extends TestHelper {
    @MockBean
    ProductDetailService productDetailService;

    @MockBean
    SimilarProductIdsService similarProductIdsService;

    @Autowired
    SimilarProductDetailService similarProductDetailService;

    @Test
    void getSimilarDetailedProductsByProductIdShouldBeOk() {
        when(similarProductIdsService.getSimilarProductIdsByProductId(PRODUCT_ID)).thenReturn(Flux.just(SIMILAR_PRODUCT_ID));
        when(productDetailService.getDetailsById(SIMILAR_PRODUCT_ID.toString())).thenReturn(Mono.just(SIMILAR_PRODUCT_DETAILED));

        Flux<SimilarProductsDetailed> result = similarProductDetailService.getSimilarDetailedProductsByProductId(PRODUCT_ID);
        assertEquals(SIMILAR_PRODUCT_DETAILED, result.blockFirst());
    }

    @Test
    void getSimilarDetailedProductsByProductIdShouldKeepSimilarityOrder() {
        when(similarProductIdsService.getSimilarProductIdsByProductId(PRODUCT_ID))
                .thenReturn(Flux.just(SIMILAR_PRODUCT_ID, SIMILAR_PRODUCT_ID_2));
        when(productDetailService.getDetailsById(SIMILAR_PRODUCT_ID.toString())).thenReturn(Mono.just(SIMILAR_PRODUCT_DETAILED));
        when(productDetailService.getDetailsById(SIMILAR_PRODUCT_ID_2.toString())).thenReturn(Mono.just(SIMILAR_PRODUCT_DETAILED_2));

        Flux<SimilarProductsDetailed> result = similarProductDetailService.getSimilarDetailedProductsByProductId(PRODUCT_ID);

        assertEquals(SIMILAR_PRODUCT_DETAILED, result.blockFirst());
        assertEquals(SIMILAR_PRODUCT_DETAILED_2, result.blockLast());
    }
}
