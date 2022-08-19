package com.tech.challenge.dtos;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Getter
@Builder
@NoArgsConstructor
public class SimilarProductsDetailed {
    private String id;
    private String name;
    private BigDecimal price;
    private boolean availability;
}