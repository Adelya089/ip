package ru.ulstu.is.server.api.genre;

import ru.ulstu.is.server.entity.GenreEntity;
public record GenreRs(Long id, String name) {
 public static GenreRs fromEntity(GenreEntity e) { return new GenreRs(e.getId(), e.getName()); }
}
