package com.novalabs.digitalbanking.account.generator;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class AccountNumberGenerator {

    private static final String PREFIX = "ACC";

    public String generate(){
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int random = ThreadLocalRandom.current()
                .nextInt(100000, 999999);
        return PREFIX + date + random;
    }
}
