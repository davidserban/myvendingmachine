package com.pastawater.vendingmachine.buyer.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class BuyResponse {
    private Integer spent;
    private Long bought;
    private Map<Integer, Integer> possibleChange;
}
