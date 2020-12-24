package com.softserve.itacademy.exception.handler;

import com.softserve.itacademy.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@ControllerAdvice
public class GenericExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException exception) {
        log.debug(exception.getMessage());
        return new ResponseEntity<>(exception.getMessage(), NOT_FOUND);
    }

}
