package com.pastawater.vendingmachine.product.controller;

import com.pastawater.vendingmachine.product.dto.CreateProductRequest;
import com.pastawater.vendingmachine.product.dto.ProductResponse;
import com.pastawater.vendingmachine.product.dto.UpdateProductRequest;
import com.pastawater.vendingmachine.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping(value = "/all")
    public List<ProductResponse> getAll() {
        return productService.findAll();
    }

    @GetMapping(value = "/{productId}")
    public ProductResponse getById(@PathVariable("productId") Long productId) {
        return productService.getById(productId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(@Valid @RequestBody CreateProductRequest request, Principal principal) {
        return productService.create(request, principal.getName());
    }

    @PutMapping(value = "/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable( "productId" ) Long productId,
                       @Valid @RequestBody UpdateProductRequest request, Principal principal) {
        productService.update(request, productId, principal.getName());
    }

    @DeleteMapping(value = "/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("productId") Long productId, Principal principal) {
        productService.deleteById(productId, principal.getName());
    }
}
