package ru.ulstu.is.server.api.artist;

import ru.ulstu.is.server.entity.ArtistEntity;
public record ArtistRs(Long id, String name, String country, ru.ulstu.is.server.api.genre.GenreRs genre) {
 public static ArtistRs fromEntity(ArtistEntity e) { return new ArtistRs(e.getId(), e.getName(), e.getCountry(), ru.ulstu.is.server.api.genre.GenreRs.fromEntity(e.getGenre())); }
}
