package com.tech.challenge.clients.rest;

import com.tech.challenge.dtos.SimilarProductsDetailed;
import reactor.core.publisher.Mono;

public interface ProductsDetailAPIClient {
    Mono<SimilarProductsDetailed> getDetailsById(String productId);
}
