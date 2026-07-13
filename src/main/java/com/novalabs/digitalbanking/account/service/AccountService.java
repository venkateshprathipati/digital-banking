package com.novalabs.digitalbanking.account.service;

import com.novalabs.digitalbanking.account.dto.AccountResponse;
import com.novalabs.digitalbanking.account.dto.CreateAccountRequest;
import com.novalabs.digitalbanking.account.dto.UpdateAccountRequest;
import com.novalabs.digitalbanking.account.entity.Account;
import com.novalabs.digitalbanking.account.enums.AccountStatus;
import com.novalabs.digitalbanking.account.enums.Currency;
import com.novalabs.digitalbanking.account.generator.AccountNumberGenerator;
import com.novalabs.digitalbanking.account.mapper.AccountMapper;
import com.novalabs.digitalbanking.account.repository.AccountRepository;
import com.novalabs.digitalbanking.common.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static com.novalabs.digitalbanking.common.constants.AccountConstants.ACCOUNT_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository repository;
    private final AccountMapper mapper;
    private final AccountNumberGenerator generator;

    public AccountResponse create(CreateAccountRequest request) {
        Account account = mapper.toEntity(request);
        account.setAccountNumber(generator.generate());
        account.setStatus(AccountStatus.ACTIVE);
        account.setBalance(BigDecimal.ZERO);
        account.setCurrency(Currency.INR);
        Account saved = repository.save(account);
        return mapper.toResponse(saved);
    }

    @Transactional
    public AccountResponse update(Long id, UpdateAccountRequest request) {
        Account account = repository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(ACCOUNT_NOT_FOUND + " with id : " + id));
        mapper.updateEntity(request, account);
        Account updated = repository.save(account);
        return mapper.toResponse(updated);
    }

    public List<AccountResponse> findAll() {
        List<Account> accounts = repository.findAll();
        return mapper.toResponse(accounts);
    }

    public AccountResponse findById(Long id) {
        Account account = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(ACCOUNT_NOT_FOUND + " : " + id));
        return mapper.toResponse(account);
    }

    public AccountResponse findByAccountNumber(String accountNumber) {
        Account account = repository
                .findByAccountNumber(accountNumber)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                ACCOUNT_NOT_FOUND));
        return mapper.toResponse(account);
    }
}
