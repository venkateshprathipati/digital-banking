package com.novalabs.digitalbanking.account.mapper;

import com.novalabs.digitalbanking.account.dto.AccountResponse;
import com.novalabs.digitalbanking.account.dto.CreateAccountRequest;
import com.novalabs.digitalbanking.account.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "status",
            expression = "java(account.getStatus().name())")
    AccountResponse toResponse(Account account);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "accountNumber", ignore = true)
    @Mapping(target = "balance", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "currency", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Account toEntity(CreateAccountRequest request);
}
