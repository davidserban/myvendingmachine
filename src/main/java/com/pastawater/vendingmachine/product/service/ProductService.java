package com.pastawater.vendingmachine.product.service;

import com.pastawater.vendingmachine.common.exception.NotFoundException;
import com.pastawater.vendingmachine.common.exception.OperationNotPermittedException;
import com.pastawater.vendingmachine.product.dto.CreateProductRequest;
import com.pastawater.vendingmachine.product.dto.ProductResponse;
import com.pastawater.vendingmachine.product.dto.UpdateProductRequest;
import com.pastawater.vendingmachine.product.model.Product;
import com.pastawater.vendingmachine.product.repository.ProductRepository;
import com.pastawater.vendingmachine.user.model.User;
import com.pastawater.vendingmachine.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public List<ProductResponse> findAll() {
        return productRepository.findAll()
                .stream()
                .map(this::constructResponse)
                .toList();
    }

    public ProductResponse getById(Long productId) {
        return productRepository.findById(productId)
                .map(this::constructResponse)
                .orElseThrow(NotFoundException::new);
    }

    public Long create(CreateProductRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(NotFoundException::new);
        return productRepository.save(constructEntity(request, user.getId())).getId();
    }

    public void update(UpdateProductRequest request, Long productId, String username) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(NotFoundException::new);
        User user = userRepository.findByUsername(username)
                .orElseThrow(NotFoundException::new);

        if(!existingProduct.getSellerId().equals(user.getId())) {
            throw new OperationNotPermittedException("Not own product");
        }

        existingProduct.setProductName(request.getProductName());
        existingProduct.setAmountAvailable(request.getAmountAvailable());
        existingProduct.setCost(request.getCost());
    }

    public void deleteById(Long productId, String username) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(NotFoundException::new);
        User user = userRepository.findByUsername(username)
                .orElseThrow(NotFoundException::new);

        if(!existingProduct.getSellerId().equals(user.getId())) {
            throw new OperationNotPermittedException("Not own product");
        }

        productRepository.deleteById(productId);
    }

    private Product constructEntity(CreateProductRequest request, Long userId) {
        return Product.builder()
                .productName(request.getProductName())
                .cost(request.getCost())
                .amountAvailable(request.getAmountAvailable())
                .sellerId(userId)
                .build();
    }

    private ProductResponse constructResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .productName(product.getProductName())
                .cost(product.getCost())
                .amountAvailable(product.getAmountAvailable())
                .sellerId(product.getSellerId())
                .build();
    }
}
