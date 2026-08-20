package com.novalabs.digitalbanking.fraud.rule;

import com.novalabs.digitalbanking.fraud.model.FraudContext;
import com.novalabs.digitalbanking.fraud.model.FraudResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class LargeAmountRule implements FraudRule {

    private static final String RULE_CODE = "LARGE_AMOUNT";

    private final BigDecimal threshold;

    public LargeAmountRule(
            @Value("${fraud.rules.large-amount.threshold:100000}")
            BigDecimal threshold
    ) {
        this.threshold = threshold;
    }

    @Override
    public FraudResult evaluate(FraudContext context) {
        if (context.amount().compareTo(threshold) > 0) {
            return FraudResult.block(
                    RULE_CODE,
                    "Transaction could not be completed."
            );
        }
        return FraudResult.pass();
    }
}
