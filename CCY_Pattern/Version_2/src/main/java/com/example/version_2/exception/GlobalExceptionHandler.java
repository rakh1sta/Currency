package com.example.version_2.exception;

import com.example.version_2.exception.customException.AlreadyCreatedException;
import com.example.version_2.exception.customException.InvalidValidationException;
import com.example.version_2.exception.customException.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;


@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InvalidValidationException.class)
    public ResponseEntity<ExceptionResponse> invalidExceptionHandler(InvalidValidationException exception, WebRequest request) {
        return new ResponseEntity<>(
                ExceptionResponse.builder()
                        .message(exception.getMessage())
                        .status(BAD_REQUEST)
                        .request(((ServletWebRequest)request).getRequest().getRequestURI())
                        .build()
                , BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionResponse> notFoundExceptionHandler(NotFoundException exception, WebRequest request) {
        return new ResponseEntity<>(
                ExceptionResponse.builder()
                        .message(exception.getMessage())
                        .status(HttpStatus.NOT_FOUND)
                        .request(((ServletWebRequest)request).getRequest().getRequestURI())
                        .build()
                ,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AlreadyCreatedException.class)
    public ResponseEntity<ExceptionResponse> alreadyCreatedException(AlreadyCreatedException exception, WebRequest request) {
        return new ResponseEntity<>(
                ExceptionResponse.builder()
                        .message(exception.getMessage())
                        .status(HttpStatus.ALREADY_REPORTED)
                        .request(((ServletWebRequest)request).getRequest().getRequestURI())
                        .build()
                ,HttpStatus.ALREADY_REPORTED);
    }




}
