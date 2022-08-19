package com.tech.challenge.services.implementations;

import com.tech.challenge.caches.ProductDetailsCache;
import com.tech.challenge.clients.rest.ProductsDetailAPIClient;
import com.tech.challenge.dtos.SimilarProductsDetailed;
import com.tech.challenge.services.ProductDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import java.util.concurrent.TimeoutException;

@Slf4j
@Service
public class ProductDetailServiceImpl implements ProductDetailService {
    private final ProductsDetailAPIClient productsDetailAPIClient;
    private final ProductDetailsCache cache;

    @Autowired
    ProductDetailServiceImpl(ProductsDetailAPIClient productsDetailAPIClient, ProductDetailsCache cache) {
        this.productsDetailAPIClient = productsDetailAPIClient;
        this.cache = cache;
    }

    public Mono<SimilarProductsDetailed> getDetailsById(String productId) {
        Mono<SimilarProductsDetailed> cachedInfo = cache.getInfo(productId);
        if (cachedInfo != null) {
            return cachedInfo;
        }
        return productsDetailAPIClient.getDetailsById(productId)
            .onErrorResume(e -> {
                handleError(e, productId);
                return Mono.empty();
            })
            .doOnNext(response -> {
                if (response != null) {
                    cache.saveInfo(response, productId);
                }
            });
    }

    private void handleError(Throwable e, String productId) {
        if (e instanceof TimeoutException) {
            log.warn("Timeout error. Will save product " + productId + " empty details to cache.");
        } else if (e instanceof WebClientResponseException) {
            HttpStatus status = ((WebClientResponseException) e).getStatusCode();
            if (status == HttpStatus.NOT_FOUND) {
                log.info("Product " + productId + " not found. Will ignore product detail.");
            } else if (status.isError()) {
                log.error("Unknown error fetching product detail with id " + productId + ". Will ignore " +
                        "product detail. Status: " + status + ".", e);
            }
        }
        cache.saveInfo(null, productId);
    }
}