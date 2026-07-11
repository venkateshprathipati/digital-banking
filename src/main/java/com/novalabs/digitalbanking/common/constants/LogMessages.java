package com.novalabs.digitalbanking.common.constants;

public final class LogMessages {

    private LogMessages() {}

    public static final String REQUEST_RECEIVED =
            "Request received: {} {}";

    public static final String REQUEST_COMPLETED =
            "Request completed: {} {}";

    public static final String ACCOUNT_CREATED =
            "Account created successfully. Account Number: {}";

    public static final String ACCOUNT_UPDATED =
            "Account updated successfully. Account Number: {}";

    public static final String ACCOUNT_FOUND =
            "Account retrieved successfully. Account Number: {}";

    public static final String ACCOUNT_NOT_FOUND =
            "Account not found. Account Number: {}";

    public static final String DATABASE_OPERATION =
            "Executing database operation: {}";

    public static final String EXCEPTION_OCCURRED =
            "Exception occurred: {}";
}
