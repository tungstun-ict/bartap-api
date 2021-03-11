package com.tungstun.security.util.validation;

import java.util.regex.Pattern;

public class NonSpaceValidator {
    /**
     * REGEX checks: does not contain whitespaces, non digits and at least 1 character
     */
    private static final String REGEX = "^[^\\d\\s]+$";

    public static boolean validate(String string){
        return Pattern.matches(REGEX, string);
    }
}
