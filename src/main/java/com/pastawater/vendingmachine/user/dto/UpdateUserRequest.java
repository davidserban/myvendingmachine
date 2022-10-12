package com.pastawater.vendingmachine.user.dto;

import com.pastawater.vendingmachine.common.security.dto.SecCharSequence;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
public class UpdateUserRequest {
    @NotEmpty
    private String username;
    @NotEmpty
    private SecCharSequence password;
}
