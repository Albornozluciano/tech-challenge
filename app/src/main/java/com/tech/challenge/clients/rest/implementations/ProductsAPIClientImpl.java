package com.tech.challenge.clients.rest.implementations;

import com.tech.challenge.clients.rest.ProductsAPIClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.time.Duration;

@Slf4j
@Component
public class ProductsAPIClientImpl implements ProductsAPIClient {

    private final WebClient webClient;

    @Autowired
    public ProductsAPIClientImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    private final static String PRODUCT_ID_PATH_PARAM = "{{productId}}";
    private final static String HOST = "http://localhost:3001";
    private final static String URI = HOST + "/product/" + PRODUCT_ID_PATH_PARAM + "/similarids";
    private final static Integer TIMEOUT = 5;

    public Mono<Integer[]> getSimilarProductIdsByProductId(String productId) {
        return webClient.get()
                .uri(URI.replace(PRODUCT_ID_PATH_PARAM, productId))
                .retrieve()
                .bodyToMono(Integer[].class)
                .timeout(Duration.ofSeconds(TIMEOUT));
    }
}


