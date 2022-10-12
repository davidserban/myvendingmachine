package com.pastawater.vendingmachine.buyer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum DepositOption {
    FIVE(5),
    TEN(10),
    TWENTY_FIVE(25),
    FIFTY(50),
    ONE_HUNDRED(100);

    private int value;

    @Override
    public String toString(){
        return String.valueOf(value);
    }

    public static DepositOption fromString(String stringValue) {
        Integer integerValue = Integer.valueOf(stringValue);
        return Arrays.stream(values())
                .filter(e -> e.value == integerValue)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
