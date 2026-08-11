package com.novalabs.digitalbanking.account.service;

import com.novalabs.digitalbanking.account.dto.*;
import com.novalabs.digitalbanking.account.entity.Account;
import com.novalabs.digitalbanking.account.enums.AccountStatus;
import com.novalabs.digitalbanking.account.enums.Currency;
import com.novalabs.digitalbanking.account.generator.AccountNumberGenerator;
import com.novalabs.digitalbanking.account.mapper.AccountMapper;
import com.novalabs.digitalbanking.account.repository.AccountRepository;
import com.novalabs.digitalbanking.common.exception.*;
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

    @Transactional
    public AccountResponse deposit(Long accountId, DepositRequest request) {
        Account account = getAccount(accountId);

        validateDeposit(account, request.amount());
        account.setBalance(
                account.getBalance().add(request.amount())
        );
        repository.save(account);
        return mapper.toResponse(account);
    }

    @Transactional
    public AccountResponse withdraw(Long accountId, WithdrawRequest request) {
        Account account = getAccountForUpdate(accountId);

        validateWithdrawal(account, request.amount());
        account.setBalance(account.getBalance().subtract(request.amount()));
        return mapper.toResponse(account);
    }

    @Transactional
    public AccountResponse freeze(Long accountId) {
        Account account = getAccountForUpdate(accountId);

        validateFreezeTransition(account);

        account.setStatus(AccountStatus.FROZEN);
        return mapper.toResponse(account);
    }

    @Transactional
    public AccountResponse unfreeze(Long accountId) {
        Account account = getAccountForUpdate(accountId);

        validateUnfreezeTransition(account);

        account.setStatus(AccountStatus.ACTIVE);
        return mapper.toResponse(account);
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

    private void validateDeposit(Account account, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidDepositAmountException("Deposit amount must be greater than zero");
        }

        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new AccountFrozenException("Account is not active");
        }
    }

    private Account getAccount(Long id) {
        return repository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(ACCOUNT_NOT_FOUND + " : " + id));
    }

    private void validateWithdrawal(Account account, BigDecimal amount) {
        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new AccountFrozenException("Account is not active");
        }

        BigDecimal balance = account.getBalance();

        if (balance.compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient balance for withdrawal");
        }
    }

    public void validateFreezeTransition(Account account) {
        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new InvalidAccountStateException(
                    "Only ACTIVE accounts can be frozen"
            );
        }
    }

    public void validateUnfreezeTransition(Account account) {
        if (account.getStatus() != AccountStatus.FROZEN) {
            throw new InvalidAccountStateException(
                    "Only FROZEN accounts can be unfrozen"
            );
        }
    }

    private Account getAccountForUpdate(Long id) {
        return repository.findByIdForUpdate(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                ACCOUNT_NOT_FOUND + " : " + id
                        ));
    }
}
