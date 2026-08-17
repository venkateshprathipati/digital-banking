package com.novalabs.digitalbanking.payment.validation;

import com.novalabs.digitalbanking.account.entity.Account;
import com.novalabs.digitalbanking.account.enums.AccountStatus;
import com.novalabs.digitalbanking.account.enums.Currency;
import com.novalabs.digitalbanking.common.exception.AccountFrozenException;
import com.novalabs.digitalbanking.common.exception.InsufficientBalanceException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AccountTransferValidator {

    public void validate(
            Account sourceAccount,
            Account destinationAccount,
            BigDecimal amount,
            Currency currency
    ) {

        if (sourceAccount.getStatus() != AccountStatus.ACTIVE) {
            throw new AccountFrozenException("Source account is not active");
        }

        if (destinationAccount.getStatus() != AccountStatus.ACTIVE) {
            throw new AccountFrozenException("Destination account is not active");
        }

        if (sourceAccount.getCurrency() != currency) {
            throw new IllegalArgumentException("Source account currency does not match transfer currency");
        }

        if (destinationAccount.getCurrency() != currency) {
            throw new IllegalArgumentException("Destination account currency does not match transfer currency");
        }

        if (sourceAccount.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient balance for transfer");
        }
    }
}
