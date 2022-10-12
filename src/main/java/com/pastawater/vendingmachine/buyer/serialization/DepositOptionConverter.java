package com.pastawater.vendingmachine.buyer.serialization;

import com.pastawater.vendingmachine.buyer.dto.DepositOption;
import org.springframework.stereotype.Component;

import org.springframework.core.convert.converter.Converter;

@Component
public class DepositOptionConverter implements Converter<String, DepositOption> {

    @Override
    public DepositOption convert(String source) {
        return DepositOption.fromString(source);
    }
}
