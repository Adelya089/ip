package ru.ulstu.is.server.error;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class AdviceController {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<AdviceErrorBody> handleNotFound(HttpServletRequest request, NotFoundException ex) {
        return new ResponseEntity<>(new AdviceErrorBody(404, ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<AdviceErrorBody> handleValidation(HttpServletRequest request, MethodArgumentNotValidException ex) {
        final String message = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return new ResponseEntity<>(new AdviceErrorBody(400, message), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(org.springframework.security.core.AuthenticationException.class)
    public ResponseEntity<AdviceErrorBody> handleAuthentication(org.springframework.security.core.AuthenticationException ex) {
        return new ResponseEntity<>(new AdviceErrorBody(401, "Authentication required or invalid credentials"), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<AdviceErrorBody> handleForbidden(org.springframework.security.access.AccessDeniedException ex) {
        return new ResponseEntity<>(new AdviceErrorBody(403, "Access denied: insufficient role"), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ru.ulstu.is.server.error.InvalidValueException.class)
    public ResponseEntity<AdviceErrorBody> handleInvalidValue(ru.ulstu.is.server.error.InvalidValueException ex) {
        return new ResponseEntity<>(new AdviceErrorBody(400, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<AdviceErrorBody> handleAnyException(HttpServletRequest request, Throwable ex) {
        return new ResponseEntity<>(new AdviceErrorBody(500, ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}