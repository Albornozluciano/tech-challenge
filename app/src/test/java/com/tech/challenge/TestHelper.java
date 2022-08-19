package com.tech.challenge;

import com.tech.challenge.dtos.SimilarProductsDetailed;

import java.math.BigDecimal;

public class TestHelper {
    protected final String PD_PREFIX_KEY = "PD#";
    protected final String SPI_PREFIX_KEY = "SPI#";
    protected final String PRODUCT_ID = "1";
    protected final String PRODUCT_NAME = "Dress";
    protected final BigDecimal PRODUCT_PRICE = BigDecimal.TEN;
    protected final Integer SIMILAR_PRODUCT_ID = 2;
    protected final Integer SIMILAR_PRODUCT_ID_2 = 3;

    protected final SimilarProductsDetailed SIMILAR_PRODUCT_DETAILED = new SimilarProductsDetailed(
            SIMILAR_PRODUCT_ID.toString(), PRODUCT_NAME, PRODUCT_PRICE, true
    );

    protected final SimilarProductsDetailed SIMILAR_PRODUCT_DETAILED_2 = new SimilarProductsDetailed(
            SIMILAR_PRODUCT_ID_2.toString(), PRODUCT_NAME, PRODUCT_PRICE, true
    );

}
