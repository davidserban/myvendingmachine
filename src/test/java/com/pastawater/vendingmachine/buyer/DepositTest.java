package com.pastawater.vendingmachine.buyer;

import com.pastawater.vendingmachine.TestConstants;
import com.pastawater.vendingmachine.TestUtil;
import com.pastawater.vendingmachine.VendingmachineApplication;
import com.pastawater.vendingmachine.user.dto.CreateUserRequest;
import com.pastawater.vendingmachine.user.dto.UserResponse;
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
public class DepositTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testDeposit_success() {

        String username = "test_deposit_success";
        CreateUserRequest createUserRequest = TestUtil.constructCreateUserRequestForBuyer(username);

        ResponseEntity<String> createResponseEntity = this.restTemplate
                .postForEntity(TestConstants.LOCALHOST + port
                        + TestConstants.USER_ENDPOINT, createUserRequest, String.class);

        ResponseEntity<String> depositResponseEntity = this.restTemplate
                .withBasicAuth(username, TestConstants.BUYER_PASSWORD)
                .postForEntity(TestConstants.LOCALHOST + port
                        + String.format(TestConstants.DEPOSIT_ENDPOINT, 10), null, String.class);

        ResponseEntity<UserResponse> depositEntityGet = this.restTemplate
                .withBasicAuth(username, TestConstants.BUYER_PASSWORD)
                .getForEntity(TestConstants.LOCALHOST + port + TestConstants.USER_ENDPOINT, UserResponse.class);

        assertEquals(201, createResponseEntity.getStatusCodeValue());
        assertEquals(202, depositResponseEntity.getStatusCodeValue());
        assertEquals(10, depositEntityGet.getBody().getDeposit());
    }

    @Test
    public void testDeposit_success_multiple() {

        String username = "test_deposit_success_multiple";
        CreateUserRequest createUserRequest = TestUtil.constructCreateUserRequestForBuyer(username);

        ResponseEntity<String> createResponseEntity = this.restTemplate
                .postForEntity(TestConstants.LOCALHOST + port
                        + TestConstants.USER_ENDPOINT, createUserRequest, String.class);

        ResponseEntity<String> depositResponseEntity = this.restTemplate
                .withBasicAuth(username, TestConstants.BUYER_PASSWORD)
                .postForEntity(TestConstants.LOCALHOST + port
                        + String.format(TestConstants.DEPOSIT_ENDPOINT, 10), null, String.class);

        ResponseEntity<String> secondDepositResponseEntity = this.restTemplate
                .withBasicAuth(username, TestConstants.BUYER_PASSWORD)
                .postForEntity(TestConstants.LOCALHOST + port
                        + String.format(TestConstants.DEPOSIT_ENDPOINT, 100), null, String.class);

        ResponseEntity<UserResponse> depositEntityGet = this.restTemplate
                .withBasicAuth(username, TestConstants.BUYER_PASSWORD)
                .getForEntity(TestConstants.LOCALHOST + port + TestConstants.USER_ENDPOINT, UserResponse.class);

        assertEquals(201, createResponseEntity.getStatusCodeValue());
        assertEquals(202, depositResponseEntity.getStatusCodeValue());
        assertEquals(202, secondDepositResponseEntity.getStatusCodeValue());
        assertEquals(110, depositEntityGet.getBody().getDeposit());
    }

    @Test
    public void testDeposit_fail_wrongValue() {

        String username = "test_deposit_fail_wrongValue";
        CreateUserRequest createUserRequest = TestUtil.constructCreateUserRequestForBuyer(username);

        ResponseEntity<String> createResponseEntity = this.restTemplate
                .postForEntity(TestConstants.LOCALHOST + port
                        + TestConstants.USER_ENDPOINT, createUserRequest, String.class);

        ResponseEntity<String> depositResponseEntity = this.restTemplate
                .withBasicAuth(username, TestConstants.BUYER_PASSWORD)
                .postForEntity(TestConstants.LOCALHOST + port
                        + String.format(TestConstants.DEPOSIT_ENDPOINT, 11), null, String.class);

        assertEquals(201, createResponseEntity.getStatusCodeValue());
        assertEquals(400, depositResponseEntity.getStatusCodeValue());
    }

    @Test
    public void testDeposit_fail_wrongRole() {

        String username = "test_deposit_fail_wrongRole";
        CreateUserRequest createUserRequest = TestUtil.constructCreateUserRequestForSeller(username);

        ResponseEntity<String> createResponseEntity = this.restTemplate
                .postForEntity(TestConstants.LOCALHOST + port
                        + TestConstants.USER_ENDPOINT, createUserRequest, String.class);

        ResponseEntity<String> depositResponseEntity = this.restTemplate
                .withBasicAuth(username, TestConstants.SELLER_PASSWORD)
                .postForEntity(TestConstants.LOCALHOST + port
                        + String.format(TestConstants.DEPOSIT_ENDPOINT, 10), null, String.class);

        assertEquals(201, createResponseEntity.getStatusCodeValue());
        assertEquals(403, depositResponseEntity.getStatusCodeValue());
    }
}

