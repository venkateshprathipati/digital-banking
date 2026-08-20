package com.novalabs.digitalbanking.fraud.engine;

import com.novalabs.digitalbanking.common.exception.BusinessRuleViolationException;
import com.novalabs.digitalbanking.fraud.model.FraudContext;
import com.novalabs.digitalbanking.fraud.model.FraudResult;
import com.novalabs.digitalbanking.fraud.rule.FraudRule;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FraudEngine {

    private final List<FraudRule> rules;

    public FraudEngine(List<FraudRule> rules) {
        this.rules = List.copyOf(rules);
    }

    public void evaluate(FraudContext context) {
        for (FraudRule rule : rules) {
            FraudResult result = rule.evaluate(context);

            if (result.blocked()) {
                throw new BusinessRuleViolationException(result.reason());
            }
        }
    }
}
