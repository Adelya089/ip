package ru.ulstu.is.server.api;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AdviceController {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<AdviceErrorBody> handleNotFound(HttpServletRequest request, NotFoundException ex) {
        return new ResponseEntity<>(new AdviceErrorBody(404, ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<AdviceErrorBody> handleAnyException(HttpServletRequest request, Throwable ex) {
        return new ResponseEntity<>(new AdviceErrorBody(500, ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}