package com.softserve.itacademy.exception.handler;

import com.softserve.itacademy.exception.DisabledObjectException;
import com.softserve.itacademy.exception.NotFoundException;
import com.softserve.itacademy.request.ErrorRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.GONE;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@ControllerAdvice
public class GenericExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleAccessDeniedException(NotFoundException exception) {
        log.error(exception.getMessage());
        ErrorRequest errorRequest = new ErrorRequest(exception.getMessage());
        return new ResponseEntity<>(errorRequest, NOT_FOUND);
    }

    @ExceptionHandler(DisabledObjectException.class)
    public ResponseEntity<ErrorRequest> handleAccessDeniedException(DisabledObjectException exception) {
        log.info(exception.getMessage());
        ErrorRequest errorRequest = new ErrorRequest(exception.getMessage());
        return new ResponseEntity<>(errorRequest, GONE);
    }

}
