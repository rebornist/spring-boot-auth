package com.widus.springbootauth.ex;

import com.widus.springbootauth.web.ResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(CustomApiException.class)
    public ResponseEntity<?> apiException(CustomApiException e) {
        logger.error(e.getMessage());
        return new ResponseEntity<>(new ResponseDto<>(-1, e.getMessage(), null), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomForbiddenException.class)
    public ResponseEntity<?> forbiddenException(CustomForbiddenException e) {
        logger.error(e.getMessage());
        return new ResponseEntity<>(new ResponseDto<>(-1, e.getMessage(), null), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(CustomValidationException.class)
    public ResponseEntity<?> validationException(CustomValidationException e) {
        logger.error(e.getMessage());
        return new ResponseEntity<>(new ResponseDto<>(-1, e.getMessage(), e.getErrorMap()), HttpStatus.BAD_REQUEST);
    }

}
