package com.pastawater.vendingmachine.user.dto;

import com.pastawater.vendingmachine.user.model.Role;
import com.pastawater.vendingmachine.common.security.dto.SecCharSequence;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
public class CreateUserRequest {
    @NotEmpty
    private String username;
    @NotEmpty
    private SecCharSequence password;
    private Role role;
}
