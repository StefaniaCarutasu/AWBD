package com.awbd.products.controller;

import com.awbd.products.exceptions.ExceptionPattern;
import com.awbd.products.exceptions.ProductNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

public class CustomizedExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ProductNotFound.class)
    protected ResponseEntity<Object> handleException(RuntimeException ex, WebRequest request) {
        ExceptionPattern exception = new ExceptionPattern(new Date(), ex.getMessage(), request.getDescription(true));
        return new ResponseEntity(exception, HttpStatus.NOT_FOUND);
    }
}
