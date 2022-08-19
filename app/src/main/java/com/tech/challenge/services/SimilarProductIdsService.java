package com.tech.challenge.services;

import reactor.core.publisher.Flux;

public interface SimilarProductIdsService {
    Flux<Integer> getSimilarProductIdsByProductId(String productId);
}