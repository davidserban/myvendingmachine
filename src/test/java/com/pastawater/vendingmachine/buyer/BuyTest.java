package com.pastawater.vendingmachine.buyer;

import com.pastawater.vendingmachine.TestConstants;
import com.pastawater.vendingmachine.TestUtil;
import com.pastawater.vendingmachine.VendingmachineApplication;
import com.pastawater.vendingmachine.buyer.dto.BuyResponse;
import com.pastawater.vendingmachine.common.exception.ApiError;
import com.pastawater.vendingmachine.product.dto.CreateProductRequest;
import com.pastawater.vendingmachine.user.dto.CreateUserRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = VendingmachineApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BuyTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testBuy_success() {

        String buyerUsername = "test_buy_success_buyer";
        String sellerUsername = "test_buy_success_seller";
        CreateUserRequest createBuyerRequest = TestUtil.constructCreateUserRequestForBuyer(buyerUsername);
        CreateUserRequest createSellerRequest = TestUtil.constructCreateUserRequestForSeller(sellerUsername);

        ResponseEntity<String> createBuyer = this.restTemplate
                .postForEntity(TestConstants.LOCALHOST + port
                        + TestConstants.USER_ENDPOINT, createBuyerRequest, String.class);

        ResponseEntity<String> createSeller = this.restTemplate
                .postForEntity(TestConstants.LOCALHOST + port
                        + TestConstants.USER_ENDPOINT, createSellerRequest, String.class);

        CreateProductRequest createProductRequest = TestUtil.constructCreateProductRequest();

        ResponseEntity<Long> createProduct = this.restTemplate
                .withBasicAuth(sellerUsername, TestConstants.SELLER_PASSWORD)
                .postForEntity(TestConstants.LOCALHOST + port
                        + TestConstants.PRODUCT_ENDPOINT, createProductRequest, Long.class);

        Long productId = createProduct.getBody();

        ResponseEntity<String> deposit = this.restTemplate
                .withBasicAuth(buyerUsername, TestConstants.BUYER_PASSWORD)
                .postForEntity(TestConstants.LOCALHOST + port
                        + String.format(TestConstants.DEPOSIT_ENDPOINT, 100), null, String.class);

        ResponseEntity<BuyResponse> buy = this.restTemplate
                .withBasicAuth(buyerUsername, TestConstants.BUYER_PASSWORD)
                .postForEntity(TestConstants.LOCALHOST + port
                        + String.format(TestConstants.BUY_ENDPOINT, productId), null, BuyResponse.class);

        assertEquals(201, createBuyer.getStatusCodeValue());
        assertEquals(201, createSeller.getStatusCodeValue());
        assertEquals(202, deposit.getStatusCodeValue());
        assertEquals(202, buy.getStatusCodeValue());
        assertEquals(productId, buy.getBody().getBought());
        assertEquals(10, buy.getBody().getSpent());
        assertEquals(0, buy.getBody().getPossibleChange().get(100));
        assertEquals(1, buy.getBody().getPossibleChange().get(50));
        assertEquals(1, buy.getBody().getPossibleChange().get(25));
        assertEquals(1, buy.getBody().getPossibleChange().get(10));
        assertEquals(1, buy.getBody().getPossibleChange().get(5));
    }

    @Test
    public void testBuy_fail_insufficientFunds() {

        String buyerUsername = "test_buy_fail_if_buyer";
        String sellerUsername = "test_buy_fail_if_seller";
        CreateUserRequest createBuyerRequest = TestUtil.constructCreateUserRequestForBuyer(buyerUsername);
        CreateUserRequest createSellerRequest = TestUtil.constructCreateUserRequestForSeller(sellerUsername);

        ResponseEntity<String> createBuyer = this.restTemplate
                .postForEntity(TestConstants.LOCALHOST + port
                        + TestConstants.USER_ENDPOINT, createBuyerRequest, String.class);

        ResponseEntity<String> createSeller = this.restTemplate
                .postForEntity(TestConstants.LOCALHOST + port
                        + TestConstants.USER_ENDPOINT, createSellerRequest, String.class);

        CreateProductRequest createProductRequest = TestUtil.constructCreateProductRequest();

        ResponseEntity<Long> createProduct = this.restTemplate
                .withBasicAuth(sellerUsername, TestConstants.SELLER_PASSWORD)
                .postForEntity(TestConstants.LOCALHOST + port
                        + TestConstants.PRODUCT_ENDPOINT, createProductRequest, Long.class);

        Long productId = createProduct.getBody();

        ResponseEntity<String> deposit = this.restTemplate
                .withBasicAuth(buyerUsername, TestConstants.BUYER_PASSWORD)
                .postForEntity(TestConstants.LOCALHOST + port
                        + String.format(TestConstants.DEPOSIT_ENDPOINT, 5), null, String.class);

        ResponseEntity<ApiError> buy = this.restTemplate
                .withBasicAuth(buyerUsername, TestConstants.BUYER_PASSWORD)
                .postForEntity(TestConstants.LOCALHOST + port
                        + String.format(TestConstants.BUY_ENDPOINT, productId), null, ApiError.class);

        assertEquals(201, createBuyer.getStatusCodeValue());
        assertEquals(201, createSeller.getStatusCodeValue());
        assertEquals(202, deposit.getStatusCodeValue());
        assertEquals(400, buy.getStatusCodeValue());
        assertEquals("Insufficient funds", buy.getBody().getMessage());
    }

    @Test
    public void testBuy_fail_outOfStock() {

        String buyerUsername = "test_buy_fail_oos_buyer";
        String sellerUsername = "test_buy_fail_oos_seller";
        CreateUserRequest createBuyerRequest = TestUtil.constructCreateUserRequestForBuyer(buyerUsername);
        CreateUserRequest createSellerRequest = TestUtil.constructCreateUserRequestForSeller(sellerUsername);

        ResponseEntity<String> createBuyer = this.restTemplate
                .postForEntity(TestConstants.LOCALHOST + port
                        + TestConstants.USER_ENDPOINT, createBuyerRequest, String.class);

        ResponseEntity<String> createSeller = this.restTemplate
                .postForEntity(TestConstants.LOCALHOST + port
                        + TestConstants.USER_ENDPOINT, createSellerRequest, String.class);

        CreateProductRequest createProductRequest = TestUtil.constructCreateProductRequest();

        ResponseEntity<Long> createProduct = this.restTemplate
                .withBasicAuth(sellerUsername, TestConstants.SELLER_PASSWORD)
                .postForEntity(TestConstants.LOCALHOST + port
                        + TestConstants.PRODUCT_ENDPOINT, createProductRequest, Long.class);

        Long productId = createProduct.getBody();

        ResponseEntity<String> deposit = this.restTemplate
                .withBasicAuth(buyerUsername, TestConstants.BUYER_PASSWORD)
                .postForEntity(TestConstants.LOCALHOST + port
                        + String.format(TestConstants.DEPOSIT_ENDPOINT, 100), null, String.class);

        ResponseEntity<BuyResponse> buy = this.restTemplate
                .withBasicAuth(buyerUsername, TestConstants.BUYER_PASSWORD)
                .postForEntity(TestConstants.LOCALHOST + port
                        + String.format(TestConstants.BUY_ENDPOINT, productId), null, BuyResponse.class);

        ResponseEntity<ApiError> secondBuy = this.restTemplate
                .withBasicAuth(buyerUsername, TestConstants.BUYER_PASSWORD)
                .postForEntity(TestConstants.LOCALHOST + port
                        + String.format(TestConstants.BUY_ENDPOINT, productId), null, ApiError.class);

        assertEquals(201, createBuyer.getStatusCodeValue());
        assertEquals(201, createSeller.getStatusCodeValue());
        assertEquals(202, deposit.getStatusCodeValue());
        assertEquals(202, buy.getStatusCodeValue());
        assertEquals(400, secondBuy.getStatusCodeValue());
        assertEquals("Product not available", secondBuy.getBody().getMessage());
    }

    @Test
    public void testBuy_fail_wrongRole() {

        String sellerUsername = "test_buy_fail_wr_seller";
        CreateUserRequest createSellerRequest = TestUtil.constructCreateUserRequestForSeller(sellerUsername);

        ResponseEntity<String> createSeller = this.restTemplate
                .postForEntity(TestConstants.LOCALHOST + port
                        + TestConstants.USER_ENDPOINT, createSellerRequest, String.class);

        CreateProductRequest createProductRequest = TestUtil.constructCreateProductRequest();

        ResponseEntity<Long> createProduct = this.restTemplate
                .withBasicAuth(sellerUsername, TestConstants.SELLER_PASSWORD)
                .postForEntity(TestConstants.LOCALHOST + port
                        + TestConstants.PRODUCT_ENDPOINT, createProductRequest, Long.class);

        Long productId = createProduct.getBody();

        ResponseEntity<BuyResponse> buy = this.restTemplate
                .withBasicAuth(sellerUsername, TestConstants.SELLER_PASSWORD)
                .postForEntity(TestConstants.LOCALHOST + port
                        + String.format(TestConstants.BUY_ENDPOINT, productId), null, BuyResponse.class);

        assertEquals(201, createSeller.getStatusCodeValue());
        assertEquals(403, buy.getStatusCodeValue());
    }
}
