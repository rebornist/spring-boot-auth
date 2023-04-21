package com.widus.springbootauth.ex;

import lombok.Getter;

import java.util.Map;

/**
 * Created by Sshs0702 on 2021. 4. 21.
 *
 * Custom Validation Exception
 */
@Getter
public class CustomValidationException extends RuntimeException {

    private Map<String, String> errorMap;

    public CustomValidationException(String message, Map<String, String> errorMap) {
        super(message);
        this.errorMap = errorMap;
    }

}
