package com.novalabs.digitalbanking.account.dto;

import com.novalabs.digitalbanking.account.enums.AccountStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateAccountRequest {

    @NotBlank(message = "Customer name is required")
    private String customerName;

    @NotNull(message = "Account status is required")
    private AccountStatus status;
}
