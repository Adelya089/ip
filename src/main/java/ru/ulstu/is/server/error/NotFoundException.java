package ru.ulstu.is.server.error;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String entity, Long id) {
        super(String.format("%s with id %s is not found", entity, id));
    }
}
