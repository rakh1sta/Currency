package com.example.cbu.exception;

import com.example.cbu.exception.customException.AlreadyCreatedException;
import com.example.cbu.exception.customException.InvalidValidationException;
import com.example.cbu.exception.customException.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.SocketTimeoutException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;


@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InvalidValidationException.class)
    public ResponseEntity<AppError> invalidExceptionHandler(InvalidValidationException exception, WebRequest request) {
        return new ResponseEntity<>(
                AppError.builder()
                        .message(exception.getMessage())
                        .status(BAD_REQUEST)
                        .request(((ServletWebRequest)request).getRequest().getRequestURI())
                        .build()
                , BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<AppError> notFoundExceptionHandler(NotFoundException exception, WebRequest request) {
        return new ResponseEntity<>(
                AppError.builder()
                        .message(exception.getMessage())
                        .status(HttpStatus.NOT_FOUND)
                        .request(((ServletWebRequest)request).getRequest().getRequestURI())
                        .build()
                ,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AlreadyCreatedException.class)
    public ResponseEntity<AppError> alreadyCreatedException(AlreadyCreatedException exception, WebRequest request) {
        return new ResponseEntity<>(
                AppError.builder()
                        .message(exception.getMessage())
                        .status(HttpStatus.ALREADY_REPORTED)
                        .request(((ServletWebRequest)request).getRequest().getRequestURI())
                        .build()
                ,HttpStatus.ALREADY_REPORTED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<AppError> internalServerErrorException(Exception exception, WebRequest request) {
        System.out.println();
        return new ResponseEntity<>(
                AppError.builder()
                        .message(exception.getMessage())
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .request(((ServletWebRequest)request).getRequest().getRequestURI())
                        .build()
                ,HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(SocketTimeoutException.class)
    public ResponseEntity<String> handleSocketTimeoutException(SocketTimeoutException e) {
        // handle the exception
        return new ResponseEntity<>("Read timeout occurred", HttpStatus.REQUEST_TIMEOUT);
    }


}
