package com.novalabs.digitalbanking.fraud.model;

public record FraudResult(
        Decision decision,
        String rulCode,
        String reason
) {

    public enum Decision {
        PASS,
        BLOCK
    }

    public static FraudResult pass() {
        return new FraudResult(
                Decision.PASS,
                null,
                null
        );
    }

    public static FraudResult block(String rulCode, String reason) {
        return new FraudResult(
                Decision.BLOCK,
                rulCode,
                reason
        );
    }

    public boolean blocked() {
        return decision == Decision.BLOCK;
    }
}
