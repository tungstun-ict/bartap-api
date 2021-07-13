package com.tungstun.security.util.validation;

import java.util.regex.Pattern;

public class NonSpaceValidator {
    /**
     * REGEX checks: does not contain whitespaces and at least 1 character
     */
    private static final String REGEX = "^[^\\s]+$";

    public static boolean validate(String string){
        return Pattern.matches(REGEX, string);
    }
}
