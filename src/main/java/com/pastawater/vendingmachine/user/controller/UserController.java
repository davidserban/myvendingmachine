package com.pastawater.vendingmachine.user.controller;

import com.pastawater.vendingmachine.user.dto.CreateUserRequest;
import com.pastawater.vendingmachine.user.dto.UpdateUserRequest;
import com.pastawater.vendingmachine.user.dto.UserResponse;
import com.pastawater.vendingmachine.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping()
    public UserResponse getCurrentUser(Principal principal) {
        return userService.get(principal.getName());
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(@Valid @RequestBody CreateUserRequest createUserRequest) {
        return userService.create(createUserRequest);
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public void update(@Valid @RequestBody UpdateUserRequest request, Principal principal) {
        userService.update(request, principal.getName());
    }

    @DeleteMapping()
    @ResponseStatus(HttpStatus.OK)
    public void delete(Principal principal) {
        userService.delete(principal.getName());
    }
}
