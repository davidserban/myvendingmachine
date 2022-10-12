package com.pastawater.vendingmachine.product.dto;

import com.pastawater.vendingmachine.product.validation.Cost;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;

@Data
@Builder
public class UpdateProductRequest {
    @PositiveOrZero
    private Integer amountAvailable;
    @Cost
    private Integer cost;
    @NotEmpty
    private String productName;
}
