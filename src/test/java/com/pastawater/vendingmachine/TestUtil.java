package com.pastawater.vendingmachine;

import com.pastawater.vendingmachine.common.security.dto.SecCharSequence;
import com.pastawater.vendingmachine.product.dto.CreateProductRequest;
import com.pastawater.vendingmachine.user.dto.CreateUserRequest;
import com.pastawater.vendingmachine.user.dto.UpdateUserRequest;
import com.pastawater.vendingmachine.user.model.Role;

public class TestUtil {

    public static CreateUserRequest constructCreateUserRequestForBuyer(String username) {
        return constructCreateUserRequest(username, Role.ROLE_BUYER, TestConstants.BUYER_PASSWORD);
    }

    public static CreateUserRequest constructCreateUserRequestForSeller(String username) {
        return constructCreateUserRequest(username, Role.ROLE_SELLER, TestConstants.SELLER_PASSWORD);
    }

    public static CreateUserRequest constructCreateUserRequest(String username, Role role, String password) {
        return CreateUserRequest.builder()
                .username(username)
                .password(new SecCharSequence(password.toCharArray()))
                .role(role)
                .build();
    }

    public static UpdateUserRequest constructUpdateUserRequest(String username, String password) {
        return UpdateUserRequest.builder()
                .username(username)
                .password(new SecCharSequence(password.toCharArray()))
                .build();
    }

    public static CreateProductRequest constructCreateProductRequest() {
        return CreateProductRequest.builder()
                .productName("productName")
                .cost(10)
                .amountAvailable(1)
                .build();
    }
}
