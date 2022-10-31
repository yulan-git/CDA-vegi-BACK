package com.vegi.vegilabback.exception;

import com.vegi.vegilabback.exception.exceptions.BusinessException;
import com.vegi.vegilabback.exception.exceptions.IngredientException;
import com.vegi.vegilabback.exception.exceptions.ResourceNotFoundException;
import com.vegi.vegilabback.exception.exceptions.TechnicalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final String ERROR_OCCURED = "An error occured";

    @ExceptionHandler({
            BusinessException.class
    })
    protected ResponseEntity<Object> handleBusinessErrorRequest(BusinessException ex, WebRequest request) {
        GlobalExceptionHandler.log.warn(ex.getLocalizedMessage());
        return handleExceptionInternal(ex, ex.getLocalizedMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({
            Error.class, Exception.class, TechnicalException.class
    })
    protected ResponseEntity<Object> handleTechnicalErrorRequest(TechnicalException ex, WebRequest request) {
        GlobalExceptionHandler.log.warn(ex.getLocalizedMessage());
        return handleExceptionInternal(ex, ERROR_OCCURED, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler({
            ResourceNotFoundException.class
    })
    protected ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        GlobalExceptionHandler.log.warn(ex.getLocalizedMessage());
        return handleExceptionInternal(ex, ex.getLocalizedMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({
            IngredientException.class
    })
    protected ResponseEntity<Object> handleIngredientException(IngredientException ex, WebRequest request) {
        GlobalExceptionHandler.log.warn(ex.getLocalizedMessage());
        return handleExceptionInternal(ex, ex.getLocalizedMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

}