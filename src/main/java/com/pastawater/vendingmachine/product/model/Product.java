package com.pastawater.vendingmachine.product.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Integer amountAvailable;
    @Column(nullable = false)
    private Integer cost;
    @Column(nullable = false)
    private String productName;
    @Column(nullable = false)
    private Long sellerId;
}
