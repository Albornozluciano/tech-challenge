package com.tech.challenge.controllers;

import com.tech.challenge.dtos.SimilarProductsDetailed;
import com.tech.challenge.services.SimilarProductDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    SimilarProductDetailService similarProductDetailService;

    @GetMapping("/{productId}/similar")
    public Flux<SimilarProductsDetailed> getSimilarProductsDetailedByProductId(@PathVariable String productId) {
        return similarProductDetailService.getSimilarDetailedProductsByProductId(productId);
    }
}