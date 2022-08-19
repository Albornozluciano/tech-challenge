package com.tech.challenge.services;

import com.tech.challenge.dtos.SimilarProductsDetailed;
import reactor.core.publisher.Mono;

public interface ProductDetailService {
    Mono<SimilarProductsDetailed> getDetailsById(String productId);
}