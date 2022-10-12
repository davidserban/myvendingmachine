package com.pastawater.vendingmachine.product.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductResponse {
    private Long id;
    private Integer amountAvailable;
    private Integer cost;
    private String productName;
    private Long sellerId;
}
