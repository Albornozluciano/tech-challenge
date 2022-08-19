package com.tech.challenge.services.implementations;

import com.tech.challenge.caches.SimilarProductIdsCache;
import com.tech.challenge.clients.rest.ProductsAPIClient;
import com.tech.challenge.dtos.NonSuccessResponse;
import com.tech.challenge.exceptions.NonSuccessException;
import com.tech.challenge.exceptions.ResponseAttributes;
import com.tech.challenge.services.SimilarProductIdsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.concurrent.TimeoutException;

@Slf4j
@Service
public class SimilarProductIdsServiceImpl implements SimilarProductIdsService {
    private final ProductsAPIClient productsAPIClient;
    private final SimilarProductIdsCache cache;

    @Autowired
    SimilarProductIdsServiceImpl(ProductsAPIClient productsAPIClient, SimilarProductIdsCache cache) {
        this.productsAPIClient = productsAPIClient;
        this.cache = cache;
    }

    public Flux<Integer> getSimilarProductIdsByProductId(String productId) {
        Flux<Integer> cachedValue = cache.getInfo(productId);
        if (cachedValue != null) {
            return cachedValue;
        }
        return productsAPIClient.getSimilarProductIdsByProductId(productId)
            .onErrorResume(e -> Mono.error(handleError(e, productId)))
            .doOnNext(response -> {
                if (response != null) {
                    cache.saveInfo(response, productId);
                }
            }).flatMapMany(Flux::fromArray);
    }

    private NonSuccessException handleError(Throwable e, String productId) {
        NonSuccessResponse nonSuccessResponse = new NonSuccessResponse(
            ResponseAttributes.UNKNOWN_SERVER_ERROR,
            "Unknown error fetching product " + productId + " similar ids from client."
        );
        if (e instanceof TimeoutException) {
            log.warn("Timeout error fetching product " + productId + " similar ids.");
        } else if (e instanceof WebClientResponseException) {
            HttpStatus status = ((WebClientResponseException) e).getStatusCode();
            if (status == HttpStatus.NOT_FOUND) {
                log.info("Product " + productId + " not found.");
                nonSuccessResponse = new NonSuccessResponse(
                        ResponseAttributes.RESOURCE_NOT_FOUND,
                        "Product " + productId + " not found."
                );
            } else if (status.isError()) {
                log.error("Unknown error fetching product " + productId + " similar ids. Status " +
                        status, e);
                nonSuccessResponse = new NonSuccessResponse(
                        ResponseAttributes.UNKNOWN_SERVER_ERROR,
                        "Unknown error fetching product " + productId + " similar ids from " +
                                "client. Response status: " + status + "."
                );
            }
        }
        return new NonSuccessException(nonSuccessResponse);
    }
}