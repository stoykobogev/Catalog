package com.catalog.handlers;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

import com.catalog.exceptions.CategoryNameAlreadyExistsException;
import com.catalog.exceptions.NoSuchEntityException;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NoSuchEntityException.class)
    public void handleNoSuchEntityException() {
        // TODO return exception message
    }
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CategoryNameAlreadyExistsException.class)
    public void handleCategoryNameAlreadyExistsException() {
        // TODO return exception message
    }
}
