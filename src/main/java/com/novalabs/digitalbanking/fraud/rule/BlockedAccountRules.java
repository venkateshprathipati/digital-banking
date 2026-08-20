package com.novalabs.digitalbanking.fraud.rule;

import com.novalabs.digitalbanking.account.enums.AccountStatus;
import com.novalabs.digitalbanking.fraud.model.FraudContext;
import com.novalabs.digitalbanking.fraud.model.FraudResult;
import org.springframework.stereotype.Component;

@Component
public class BlockedAccountRules implements FraudRule {

    private static final String RULE_CODE = "BLOCKED_ACCOUNT";

    @Override
    public FraudResult evaluate(FraudContext context) {
        if (isBlocked(context.sourceAccount().getStatus()) ||
                isBlocked(context.destinationAccount().getStatus())) {
            return FraudResult.block(
                    RULE_CODE,
                    "Transaction involves a blocked account"
            );
        }
        return FraudResult.pass();
    }

    private boolean isBlocked(AccountStatus status) {
        return status == AccountStatus.FROZEN || status == AccountStatus.CLOSED;
    }
}
