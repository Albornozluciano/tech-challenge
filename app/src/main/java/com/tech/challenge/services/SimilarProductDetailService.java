package com.tech.challenge.services;

import com.tech.challenge.dtos.SimilarProductsDetailed;
import reactor.core.publisher.Flux;

public interface SimilarProductDetailService {
    Flux<SimilarProductsDetailed> getSimilarDetailedProductsByProductId(String productId);
}