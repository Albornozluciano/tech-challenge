package com.tech.challenge.controllers;

import com.tech.challenge.TestHelper;
import com.tech.challenge.dtos.SimilarProductsDetailed;
import com.tech.challenge.services.SimilarProductDetailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@WebFluxTest(ProductController.class)
public class ProductControllerTest extends TestHelper {
    @MockBean
    SimilarProductDetailService similarProductDetailService;

    @Autowired
    ProductController productController;

    @Test
    void getSimilarProductsDetailedByProductId() {
        when(similarProductDetailService.getSimilarDetailedProductsByProductId(PRODUCT_ID))
                .thenReturn(Flux.just(SIMILAR_PRODUCT_DETAILED));

        Flux<SimilarProductsDetailed> result = productController.getSimilarProductsDetailedByProductId(PRODUCT_ID);

        assertEquals(SIMILAR_PRODUCT_DETAILED, result.blockFirst());
    }
}
