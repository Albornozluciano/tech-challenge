package com.tech.challenge.services.implementations;

import com.tech.challenge.dtos.SimilarProductsDetailed;
import com.tech.challenge.services.ProductDetailService;
import com.tech.challenge.services.SimilarProductDetailService;
import com.tech.challenge.services.SimilarProductIdsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Slf4j
@Service
public class SimilarProductDetailServiceImpl implements SimilarProductDetailService {
    @Autowired
    SimilarProductIdsService similarProductIdsService;
    @Autowired
    ProductDetailService productDetailService;

    public Flux<SimilarProductsDetailed> getSimilarDetailedProductsByProductId(String productId) {
        return similarProductIdsService.getSimilarProductIdsByProductId(productId)
            .flatMapSequential(id -> productDetailService.getDetailsById(id.toString()));
    }
}