package com.softserve.itacademy.exception.handler;

import com.softserve.itacademy.exception.DisabledObjectException;
import com.softserve.itacademy.exception.FileHasNoExtensionException;
import com.softserve.itacademy.exception.FileProcessingException;
import com.softserve.itacademy.exception.NotFoundException;
import com.softserve.itacademy.exception.OperationNotAllowedException;
import com.softserve.itacademy.exception.dto.BasicExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GenericExceptionHandler {

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<BasicExceptionResponse> handleNotFoundException(NotFoundException exception) {

        BasicExceptionResponse dto = BasicExceptionResponse.builder()
                .message(exception.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .error(exception.getClass().getSimpleName())
                .build();

        return new ResponseEntity<>(dto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({DisabledObjectException.class})
    public ResponseEntity<BasicExceptionResponse> handleDisabledObjectException(DisabledObjectException exception) {

        BasicExceptionResponse dto = BasicExceptionResponse.builder()
                .message(exception.getMessage())
                .status(HttpStatus.GONE.value())
                .error(exception.getClass().getSimpleName())
                .build();

        return new ResponseEntity<>(dto, HttpStatus.GONE);
    }

    @ExceptionHandler({FileHasNoExtensionException.class})
    public ResponseEntity<BasicExceptionResponse> handleFileHasNoExtensionException(FileHasNoExtensionException exception) {

        BasicExceptionResponse dto = BasicExceptionResponse.builder()
                .message(exception.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(exception.getClass().getSimpleName())
                .build();

        return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({FileProcessingException.class})
    public ResponseEntity<BasicExceptionResponse> handleFileProcessingException(FileProcessingException exception) {

        BasicExceptionResponse dto = BasicExceptionResponse.builder()
                .message(exception.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(exception.getClass().getSimpleName())
                .build();

        return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @MessageExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<BasicExceptionResponse> handleValidationException(MethodArgumentNotValidException exception) {

        ObjectError error = exception.getAllErrors().get(0);

        String message = exception.getFieldError().getField() + " " + error.getDefaultMessage();

        BasicExceptionResponse dto = BasicExceptionResponse.builder()
                .message(message)
                .status(HttpStatus.BAD_REQUEST.value())
                .error(exception.getClass().getSimpleName())
                .build();

        return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<BasicExceptionResponse> handleAuthException(AuthenticationException exception) {

        BasicExceptionResponse dto = BasicExceptionResponse.builder()
                .message(exception.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(exception.getClass().getSimpleName())
                .build();

        return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({OperationNotAllowedException.class})
    public ResponseEntity<BasicExceptionResponse> handleOperationNotAllowedException(OperationNotAllowedException exception) {

        BasicExceptionResponse dto = BasicExceptionResponse.builder()
                .message(exception.getMessage())
                .status(HttpStatus.METHOD_NOT_ALLOWED.value())
                .error(exception.getClass().getSimpleName())
                .build();

        return new ResponseEntity<>(dto, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler({AccessDeniedException.class})
    @MessageExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<BasicExceptionResponse> handleAccessDeniedException(AccessDeniedException exception) {

        BasicExceptionResponse dto = BasicExceptionResponse.builder()
                .message(exception.getMessage())
                .status(HttpStatus.FORBIDDEN.value())
                .error(exception.getClass().getSimpleName())
                .build();

        return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
    }
}
