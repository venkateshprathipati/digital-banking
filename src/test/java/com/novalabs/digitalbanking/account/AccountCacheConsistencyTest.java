package com.novalabs.digitalbanking.account;

import com.novalabs.digitalbanking.account.dto.AccountResponse;
import com.novalabs.digitalbanking.account.dto.DepositRequest;
import com.novalabs.digitalbanking.account.entity.Account;
import com.novalabs.digitalbanking.account.enums.AccountStatus;
import com.novalabs.digitalbanking.account.enums.Currency;
import com.novalabs.digitalbanking.account.generator.AccountNumberGenerator;
import com.novalabs.digitalbanking.account.mapper.AccountMapper;
import com.novalabs.digitalbanking.account.repository.AccountRepository;
import com.novalabs.digitalbanking.account.service.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountCacheConsistencyTest {

    @Mock
    private AccountRepository repository;

    @Mock
    private AccountMapper mapper;

    @Mock
    private AccountNumberGenerator generator;

    @InjectMocks
    private AccountService accountService;

    @Test
    void findById_shouldReadFromRepositoryWhenCacheMisses() {
        Account account = account(1000L, "ACC001", new BigDecimal("1000.00"));
        AccountResponse response = response(1000L, new BigDecimal("1000.00"));

        when(repository.findById(1000L)).thenReturn(Optional.of(account));
        when(mapper.toResponse(account)).thenReturn(response);

        AccountResponse result = accountService.findById(1000L);

        assertThat(result).isSameAs(response);
        verify(repository).findById(1000L);
        verify(mapper).toResponse(account);
    }

    @Test
    void deposit_shouldPersistTheNewBalance() {
        Account account = account(1000L, "ACC001", new BigDecimal("1000.00"));
        AccountResponse response = response(1000L, new BigDecimal("1500.00"));

        when(repository.findById(1000L)).thenReturn(Optional.of(account));
        when(mapper.toResponse(account)).thenReturn(response);
        when(repository.save(account)).thenReturn(account);

        AccountResponse result = accountService.deposit(
                1000L,
                new DepositRequest(new BigDecimal("500.00")));

        assertThat(account.getBalance()).isEqualByComparingTo("1500.00");
        assertThat(result).isSameAs(response);
        verify(repository).save(account);
    }

    private Account account(Long id, String accountNumber, BigDecimal balance) {
        Account account = new Account();
        account.setId(id);
        account.setAccountNumber(accountNumber);
        account.setBalance(balance);
        account.setStatus(AccountStatus.ACTIVE);
        account.setCurrency(Currency.INR);
        return account;
    }

    private AccountResponse response(Long id, BigDecimal balance) {
        return new AccountResponse("ACC001", id, balance, AccountStatus.ACTIVE.name());
    }
}
