package com.regain.auth_service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandler {
    Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);

    @org.springframework.web.bind.annotation.ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> conflictData(Exception e) {
        logger.info(e.getMessage());
        Map<String, String> map = new HashMap<>();
        map.put("code", "409");
        map.put("error", "Conflict Data");
        return map;
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public Map<String, String> methodNotAllowed(Exception e) {
        logger.info(e.getMessage());
        Map<String, String> map = new HashMap<>();
        map.put("code", "405");
        map.put("error", "Method Not Allowed");
        return map;
    }


    @org.springframework.web.bind.annotation.ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> badRequest(Exception e) {
        logger.info(e.getMessage());
        Map<String, String> map = new HashMap<>();
        map.put("code", "400");
        map.put("error", "Parameter are not valid");
        return map;
    }
}
