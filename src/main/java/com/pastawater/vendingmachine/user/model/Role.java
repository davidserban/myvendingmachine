package com.pastawater.vendingmachine.user.model;

public enum Role {
    ROLE_BUYER,
    ROLE_SELLER,
    ROLE_ADMIN; //this would make sense on the long run

    @Override
    public String toString() {
        return name();
    }
}
