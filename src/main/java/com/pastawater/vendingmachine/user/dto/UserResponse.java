package com.pastawater.vendingmachine.user.dto;

import com.pastawater.vendingmachine.user.model.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private Role role;
    private Integer deposit;
}
