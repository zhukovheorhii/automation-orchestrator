package com.automation.orchestrator.advice;

import com.automation.orchestrator.exception.NoTestUserAvailableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ControllerAdvise {
    private static final Logger log = LoggerFactory.getLogger(ControllerAdvise.class);

    @ExceptionHandler(NoTestUserAvailableException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleNotFoundExceptions(NoTestUserAvailableException exception) {
        log.error("No test user available.", exception);
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void handleIllegalStateException(IllegalStateException ex) {
        log.error("Illegal state encountered: {}", ex.getMessage(), ex);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void handleException(Exception ex) {
        log.error("Unexpected error occurred: {}", ex.getMessage(), ex);
    }
}
