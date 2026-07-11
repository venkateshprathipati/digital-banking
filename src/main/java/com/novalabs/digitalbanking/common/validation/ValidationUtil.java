package com.novalabs.digitalbanking.common.validation;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ValidationUtil {

    public static boolean hasText(String value){
        return value != null && !value.trim().isEmpty();
    }
}
