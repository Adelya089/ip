package ru.ulstu.is.server.api;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String entity, String id) {
        super(String.format("%s with id %s is not found", entity, id));
    }
}