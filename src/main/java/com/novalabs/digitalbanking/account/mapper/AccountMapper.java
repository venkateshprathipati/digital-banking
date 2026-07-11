package com.novalabs.digitalbanking.account.mapper;

import com.novalabs.digitalbanking.account.dto.AccountResponse;
import com.novalabs.digitalbanking.account.dto.CreateAccountRequest;
import com.novalabs.digitalbanking.account.dto.UpdateAccountRequest;
import com.novalabs.digitalbanking.account.entity.Account;
import com.novalabs.digitalbanking.account.enums.AccountStatus;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "status", source = "status")
    AccountResponse toResponse(Account account);

    List<AccountResponse> toResponse(List<Account> accounts);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "accountNumber", ignore = true)
    @Mapping(target = "balance", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "currency", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Account toEntity(CreateAccountRequest request);

    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
    )
    void updateEntity(UpdateAccountRequest request, @MappingTarget Account account);

    default String mapStatus(AccountStatus status) {
        return status.name();
    }
}
