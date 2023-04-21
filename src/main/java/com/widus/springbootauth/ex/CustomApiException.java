package com.widus.springbootauth.ex;

/**
 * Created by Sshs0702 on 2021. 4. 21.
 *
 * Custom Exception
 */
public class CustomApiException extends RuntimeException {

    public CustomApiException(String message) {
        super(message);
    }

}
