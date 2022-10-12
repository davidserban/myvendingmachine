package com.pastawater.vendingmachine.user.service;

import com.pastawater.vendingmachine.user.dto.CreateUserRequest;
import com.pastawater.vendingmachine.user.dto.UpdateUserRequest;
import com.pastawater.vendingmachine.common.exception.NotFoundException;
import com.pastawater.vendingmachine.user.dto.UserResponse;
import com.pastawater.vendingmachine.user.model.User;
import com.pastawater.vendingmachine.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User getById(Long id) {
        return userRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public UserResponse get(String username) {
        return userRepository.findByUsername(username)
                .map(this::constructResponse)
                .orElseThrow(NotFoundException::new);
    }

    public Long create(CreateUserRequest request) {
        User user = constructEntity(request);
        request.getPassword().clear();
        return userRepository.save(user).getId();
    }

    public void update(UpdateUserRequest request, String username) {
        User user = getEntity(username);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        request.getPassword().clear();
        user.setUsername(request.getUsername());
    }

    public void delete(String username) {
        userRepository.delete(getEntity(username));
    }

    private User getEntity(String username) {
        return userRepository.findByUsername(username).orElseThrow(NotFoundException::new);
    }

    private User constructEntity(CreateUserRequest request) {
        return User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .deposit(0)
                .build();
    }

    private UserResponse constructResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .deposit(user.getDeposit())
                .build();
    }
}
