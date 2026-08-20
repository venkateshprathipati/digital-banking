package com.novalabs.digitalbanking.fraud.rule;

import com.novalabs.digitalbanking.fraud.model.FraudContext;
import com.novalabs.digitalbanking.fraud.model.FraudResult;

public interface FraudRule {

    FraudResult evaluate(FraudContext context);

}
