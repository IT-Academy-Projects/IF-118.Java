package com.softserve.itacademy.exception.handler;

import com.softserve.itacademy.config.ErrorConfigurationProperties;
import com.softserve.itacademy.dto.ErrorDto;
import com.softserve.itacademy.enums.ErrorType;
import com.softserve.itacademy.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@ControllerAdvice
public class GenericExceptionHandler {

    private final ErrorConfigurationProperties errorConfigurationProperties;

    public GenericExceptionHandler(ErrorConfigurationProperties errorConfigurationProperties) {
        this.errorConfigurationProperties = errorConfigurationProperties;
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleAccessDeniedException(NotFoundException exception) {
        log.error(exception.getMessage());
        ErrorDto errorDto = new ErrorDto(errorConfigurationProperties.getExceptions()
                .get(ErrorType.NOT_FOUND.toString()).getMessage());
        return new ResponseEntity<>(errorDto, NOT_FOUND);
    }

}
