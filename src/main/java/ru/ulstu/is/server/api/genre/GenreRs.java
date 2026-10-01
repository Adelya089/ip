package ru.ulstu.is.server.api.genre;

import ru.ulstu.is.server.entity.GenreEntity;

public record GenreRs(Long id, String name) {
    public static GenreRs fromEntity(GenreEntity entity) {
        return new GenreRs(entity.getId(), entity.getName());
    }
}