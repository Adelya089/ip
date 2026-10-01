package ru.ulstu.is.server.api.album;

import ru.ulstu.is.server.api.artist.ArtistRs;
import ru.ulstu.is.server.entity.AlbumEntity;

public record AlbumRs(
        Long id,
        String title,
        Integer year,
        ArtistRs artist,
        Integer trackCount,
        String cover) {
    public static AlbumRs fromEntity(AlbumEntity entity) {
        final var artist = ArtistRs.fromEntity(entity.getArtist());
        return new AlbumRs(
                entity.getId(),
                entity.getTitle(),
                entity.getYear(),
                artist,
                entity.getTrackCount(),
                entity.getCover());
    }
}