package com.pastawater.vendingmachine.product.repository;

import com.pastawater.vendingmachine.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}

