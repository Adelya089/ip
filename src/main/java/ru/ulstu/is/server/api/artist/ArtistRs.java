package ru.ulstu.is.server.api.artist;

import ru.ulstu.is.server.api.genre.GenreRs;
import ru.ulstu.is.server.entity.ArtistEntity;

public record ArtistRs(
        Long id,
        String name,
        String country,
        GenreRs genre) {
    public static ArtistRs fromEntity(ArtistEntity entity) {
        final var genre = GenreRs.fromEntity(entity.getGenre());
        return new ArtistRs(
                entity.getId(),
                entity.getName(),
                entity.getCountry(),
                genre);
    }
}