package com.tech.challenge.clients.rest;

import reactor.core.publisher.Mono;

public interface ProductsAPIClient {
    Mono<Integer[]> getSimilarProductIdsByProductId(String productId);
}
