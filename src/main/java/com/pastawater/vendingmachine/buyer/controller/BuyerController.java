package com.pastawater.vendingmachine.buyer.controller;

import com.pastawater.vendingmachine.buyer.dto.BuyResponse;
import com.pastawater.vendingmachine.buyer.dto.DepositOption;
import com.pastawater.vendingmachine.buyer.service.BuyerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class BuyerController {

    private final BuyerService buyerService;

    @PostMapping(value = "/buy/{productId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public BuyResponse buy(@PathVariable( "productId" ) Long productId, Principal principal) {
        return buyerService.buy(productId, principal.getName());
    }

    @PostMapping(value = "/reset")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void reset(Principal principal) {
        buyerService.reset(principal.getName());
    }

    @PostMapping(value = "/deposit/{amount}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deposit(@PathVariable( "amount" ) DepositOption amount, Principal principal) {
        buyerService.deposit(amount, principal.getName());
    }
}
