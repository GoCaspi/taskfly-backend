package com.gocaspi.taskfly.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.ConstraintViolationException;


import java.util.HashMap;
import java.util.Map;
/**
 * Class for ApiExceptionHandler
 */
@RestControllerAdvice
public class ApiExceptionHandler {
    public static final String ERRORMSGKEY = "error_msg";

    /**
     * This method is called whenever another method returns Not Found Exception
     * @param na HttpClientErrorException NotFound
     * @return returns a map that returns an error message as described in the method
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    public Map<String, String> handleResourceNotFound(HttpClientErrorException.NotFound na){
        Map<String,String> errorMap = new HashMap<>();
        errorMap.put("local_msg",na.getLocalizedMessage());
        errorMap.put("status_txt",na.getStatusText());
        errorMap.put(ERRORMSGKEY,"the requested resource was not found in the database");
        return errorMap;
    }

    /**
     * This method is called whenever another method returns Bad Request Exception
     * @param na HttpClientErrorException BadRequest
     * @return returns a map that returns an error message as described in the method
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpClientErrorException.BadRequest.class)
    public Map<String, String> handleBadRequest(HttpClientErrorException.BadRequest na){
        Map<String,String> errorMap = new HashMap<>();
        errorMap.put("local_msg",na.getLocalizedMessage());
        errorMap.put("status_txt",na.getStatusText());
        errorMap.put(ERRORMSGKEY,"bad payload: missing field values in the provided data");
        return errorMap;
    }

    /**
     * This method is called whenever another method returns Bad Request Exception
     * @param ce ConstraintViolationException
     * @return returns a map that returns an error message as described in the method
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public Map<String, String> handleConstraintError(ConstraintViolationException ce){
        Map<String, String> errors = new HashMap<>();
        errors.put(ERRORMSGKEY, "request validation failed, please fix the issues");
        ce.getConstraintViolations().forEach(error -> errors.put(error.getPropertyPath().toString(), error.getMessage()));
        return errors;
    }
}
