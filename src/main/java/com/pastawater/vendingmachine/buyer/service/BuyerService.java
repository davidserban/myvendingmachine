package com.pastawater.vendingmachine.buyer.service;

import com.pastawater.vendingmachine.buyer.dto.BuyResponse;
import com.pastawater.vendingmachine.buyer.dto.DepositOption;
import com.pastawater.vendingmachine.buyer.exception.BuyerException;
import com.pastawater.vendingmachine.common.exception.NotFoundException;
import com.pastawater.vendingmachine.product.model.Product;
import com.pastawater.vendingmachine.product.repository.ProductRepository;
import com.pastawater.vendingmachine.user.model.User;
import com.pastawater.vendingmachine.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class BuyerService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public BuyResponse buy (Long productId, String username) {
        User buyer = userRepository.findByUsername(username)
                .orElseThrow(NotFoundException::new);
        Product product = productRepository.findById(productId)
                .orElseThrow(NotFoundException::new);
        User seller = userRepository.findById(product.getSellerId())
                .orElseThrow(NotFoundException::new);

        if (product.getAmountAvailable() < 1) {
            throw new BuyerException("Product not available");
        }
        if (buyer.getDeposit() < product.getCost()) {
            throw new BuyerException("Insufficient funds");
        }

        product.setAmountAvailable(product.getAmountAvailable() - 1);
        buyer.setDeposit(buyer.getDeposit() - product.getCost());
        seller.setDeposit(seller.getDeposit() + product.getCost());

        return BuyResponse.builder()
                .spent(product.getCost())
                .bought(productId)
                .possibleChange(calculateChangeOptions(buyer.getDeposit()))
                .build();
    }

    public void reset(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(NotFoundException::new);
        user.setDeposit(0);
    }

    public void deposit(DepositOption amount, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(NotFoundException::new);
        user.setDeposit(user.getDeposit() + amount.getValue());
    }

    private Map<Integer, Integer> calculateChangeOptions(int remainingDeposit) {
        Map<Integer, Integer> result = new HashMap<>();

        for(Integer item : getCurrencyItemsAsList()) {
            int count  = 0;
            while(remainingDeposit >= item) {
                remainingDeposit -= item;
                count++;
            }
            result.put(item, count);
        }
        return result;
    }

    private List<Integer> getCurrencyItemsAsList() {
        return Arrays.stream(DepositOption.values())
                .map(DepositOption::getValue)
                .sorted(Comparator.reverseOrder())
                .toList();
    }
}
