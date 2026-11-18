package ru.ulstu.is.server.api.artist;

import ru.ulstu.is.server.entity.ArtistEntity;
import ru.ulstu.is.server.api.genre.GenreRs;

public record ArtistRs(Long id, String name, String country, GenreRs genre, String photo) {
    public static ArtistRs fromEntity(ArtistEntity e) {
        return new ArtistRs(e.getId(), e.getName(), e.getCountry(),
                GenreRs.fromEntity(e.getGenre()), e.getPhoto());
    }
}
