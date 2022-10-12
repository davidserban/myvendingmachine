package com.pastawater.vendingmachine.user;

import com.pastawater.vendingmachine.TestConstants;
import com.pastawater.vendingmachine.TestUtil;
import com.pastawater.vendingmachine.VendingmachineApplication;
import com.pastawater.vendingmachine.user.dto.CreateUserRequest;
import com.pastawater.vendingmachine.user.dto.UpdateUserRequest;
import com.pastawater.vendingmachine.user.dto.UserResponse;
import com.pastawater.vendingmachine.user.model.Role;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = VendingmachineApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testCreateUser_success() {

        String username = "test_create_success";
        CreateUserRequest createUserRequest = TestUtil.constructCreateUserRequestForBuyer(username);

        ResponseEntity<String> responseEntity = this.restTemplate
                        .postForEntity(TestConstants.LOCALHOST + port
                                + TestConstants.USER_ENDPOINT, createUserRequest, String.class);

        assertEquals(201, responseEntity.getStatusCodeValue());
    }

    @Test
    public void testCreateUser_fail_duplicate() {

        String username = "test_create_fail_duplicate";
        CreateUserRequest createUserRequest = TestUtil.constructCreateUserRequestForBuyer(username);

        ResponseEntity<String> responseEntityFirst = this.restTemplate
                .postForEntity(TestConstants.LOCALHOST + port
                        + TestConstants.USER_ENDPOINT, createUserRequest, String.class);
        ResponseEntity<String> responseEntitySecond = this.restTemplate
                .postForEntity(TestConstants.LOCALHOST + port
                        + TestConstants.USER_ENDPOINT, createUserRequest, String.class);

        assertEquals(201, responseEntityFirst.getStatusCodeValue());
        assertEquals(412, responseEntitySecond.getStatusCodeValue());
    }

    @Test
    public void testCreateUser_fail_emptyPassword() {

        String username = "test_create_fail_emptyPassword";
        CreateUserRequest createUserRequest = TestUtil.constructCreateUserRequestForBuyer(username);
        createUserRequest.setPassword(null);

        ResponseEntity<String> responseEntity = this.restTemplate
                .postForEntity(TestConstants.LOCALHOST + port
                        + TestConstants.USER_ENDPOINT, createUserRequest, String.class);

        assertEquals(400, responseEntity.getStatusCodeValue());
    }

    @Test
    public void testCreateUser_fail_noRole() {

        String username = "test_create_fail_noRole";
        CreateUserRequest createUserRequest = TestUtil.constructCreateUserRequestForBuyer(username);
        createUserRequest.setRole(null);

        ResponseEntity<String> responseEntity = this.restTemplate
                .postForEntity(TestConstants.LOCALHOST + port
                        + TestConstants.USER_ENDPOINT, createUserRequest, String.class);

        assertEquals(412, responseEntity.getStatusCodeValue());
    }

    @Test
    public void testGetUser_success() {

        String username = "test_get_success";

        CreateUserRequest createUserRequest = TestUtil.constructCreateUserRequestForBuyer(username);

        ResponseEntity<String> responseEntityCreate = this.restTemplate
                .postForEntity(TestConstants.LOCALHOST + port
                        + TestConstants.USER_ENDPOINT, createUserRequest, String.class);

        ResponseEntity<UserResponse> responseEntityGet = this.restTemplate
                .withBasicAuth(username, TestConstants.BUYER_PASSWORD)
                .getForEntity(TestConstants.LOCALHOST + port
                        + TestConstants.USER_ENDPOINT, UserResponse.class);


        assertEquals(201, responseEntityCreate.getStatusCodeValue());
        assertEquals(200, responseEntityGet.getStatusCodeValue());
        assertEquals(username, responseEntityGet.getBody().getUsername());
        assertEquals(0, responseEntityGet.getBody().getDeposit());
        assertEquals(Role.ROLE_BUYER, responseEntityGet.getBody().getRole());
        assertNotNull(responseEntityGet.getBody().getId());
    }


    @Test
    public void testUpdateUser_success() {

        String username = "test_update_success";

        CreateUserRequest createUserRequest = TestUtil.constructCreateUserRequestForBuyer(username);

        ResponseEntity<String> responseEntityCreate = this.restTemplate
                .postForEntity(TestConstants.LOCALHOST + port
                        + TestConstants.USER_ENDPOINT, createUserRequest, String.class);

        UpdateUserRequest updateUserRequest = TestUtil.constructUpdateUserRequest(username, "modified");
        HttpEntity<UpdateUserRequest> updateUserRequestHttpEntity = new HttpEntity<>(updateUserRequest);

        ResponseEntity<String> responseEntityUpdate = this.restTemplate
                .withBasicAuth(username, TestConstants.BUYER_PASSWORD)
                .exchange(TestConstants.LOCALHOST + port
                        + TestConstants.USER_ENDPOINT, HttpMethod.PUT, updateUserRequestHttpEntity, String.class);

        ResponseEntity<UserResponse> responseEntityGet = this.restTemplate
                .withBasicAuth(username, "modified")
                .getForEntity(TestConstants.LOCALHOST + port
                        + TestConstants.USER_ENDPOINT, UserResponse.class);


        assertEquals(201, responseEntityCreate.getStatusCodeValue());
        assertEquals(200, responseEntityUpdate.getStatusCodeValue());
        assertEquals(200, responseEntityGet.getStatusCodeValue());
        assertEquals(username, responseEntityGet.getBody().getUsername());
    }

    @Test
    public void testUpdateUser_fail_wrongPassword() {

        String username = "test_update_fail";

        CreateUserRequest createUserRequest = TestUtil.constructCreateUserRequestForBuyer(username);

        ResponseEntity<String> responseEntityCreate = this.restTemplate
                .postForEntity(TestConstants.LOCALHOST + port
                        + TestConstants.USER_ENDPOINT, createUserRequest, String.class);

        UpdateUserRequest updateUserRequest = TestUtil.constructUpdateUserRequest(username, "modified");
        HttpEntity<UpdateUserRequest> updateUserRequestHttpEntity = new HttpEntity<>(updateUserRequest);

        ResponseEntity<String> responseEntityUpdate = this.restTemplate
                .withBasicAuth(username, "wrongpassword")
                .exchange(TestConstants.LOCALHOST + port
                        + TestConstants.USER_ENDPOINT, HttpMethod.PUT, updateUserRequestHttpEntity, String.class);


        assertEquals(201, responseEntityCreate.getStatusCodeValue());
        assertEquals(401, responseEntityUpdate.getStatusCodeValue());
    }

    @Test
    public void testUpdateUser_fail_emptyUsername() {

        String username = "test_update_fail_empty_username";

        CreateUserRequest createUserRequest = TestUtil.constructCreateUserRequestForBuyer(username);

        ResponseEntity<String> responseEntityCreate = this.restTemplate
                .postForEntity(TestConstants.LOCALHOST + port
                        + TestConstants.USER_ENDPOINT, createUserRequest, String.class);

        UpdateUserRequest updateUserRequest = TestUtil.constructUpdateUserRequest(null, "modified");
        HttpEntity<UpdateUserRequest> updateUserRequestHttpEntity = new HttpEntity<>(updateUserRequest);

        ResponseEntity<String> responseEntityUpdate = this.restTemplate
                .withBasicAuth(username, TestConstants.BUYER_PASSWORD)
                .exchange(TestConstants.LOCALHOST + port
                        + TestConstants.USER_ENDPOINT, HttpMethod.PUT, updateUserRequestHttpEntity, String.class);


        assertEquals(201, responseEntityCreate.getStatusCodeValue());
        assertEquals(400, responseEntityUpdate.getStatusCodeValue());
    }

    @Test
    public void testDeleteUser_success() {

        String username = "test_delete_success";

        CreateUserRequest createUserRequest = TestUtil.constructCreateUserRequestForBuyer(username);

        ResponseEntity<String> responseEntityCreate = this.restTemplate
                .postForEntity(TestConstants.LOCALHOST + port
                        + TestConstants.USER_ENDPOINT, createUserRequest, String.class);

        ResponseEntity<String> responseEntityDelete = this.restTemplate
                .withBasicAuth(username, TestConstants.BUYER_PASSWORD)
                .exchange(TestConstants.LOCALHOST + port
                        + TestConstants.USER_ENDPOINT, HttpMethod.DELETE, null, String.class);

        ResponseEntity<UserResponse> responseEntityGet = this.restTemplate
                .withBasicAuth(username, TestConstants.BUYER_PASSWORD)
                .getForEntity(TestConstants.LOCALHOST + port
                        + TestConstants.USER_ENDPOINT, UserResponse.class);


        assertEquals(201, responseEntityCreate.getStatusCodeValue());
        assertEquals(200, responseEntityDelete.getStatusCodeValue());
        assertEquals(401, responseEntityGet.getStatusCodeValue());
    }

    @Test
    public void testDeleteUser_fail_wrongPassword() {

        String username = "test_delete_fail_wrongPassword";

        CreateUserRequest createUserRequest = TestUtil.constructCreateUserRequestForBuyer(username);

        ResponseEntity<String> responseEntityCreate = this.restTemplate
                .postForEntity(TestConstants.LOCALHOST + port
                        + TestConstants.USER_ENDPOINT, createUserRequest, String.class);

        ResponseEntity<String> responseEntityDelete = this.restTemplate
                .withBasicAuth(username, "wrongpassword")
                .exchange(TestConstants.LOCALHOST + port
                        + TestConstants.USER_ENDPOINT, HttpMethod.DELETE, null, String.class);

        assertEquals(201, responseEntityCreate.getStatusCodeValue());
        assertEquals(401, responseEntityDelete.getStatusCodeValue());
    }
}
