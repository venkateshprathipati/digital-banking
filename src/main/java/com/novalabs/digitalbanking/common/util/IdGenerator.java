package com.novalabs.digitalbanking.common.util;

import java.util.UUID;

public final class IdGenerator {

    private IdGenerator() {}

    public static String generateRequestId() {
        return UUID.randomUUID().toString();
    }
}
