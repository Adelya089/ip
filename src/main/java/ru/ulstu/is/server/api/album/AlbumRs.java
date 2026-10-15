package ru.ulstu.is.server.api.album;

import ru.ulstu.is.server.entity.AlbumEntity;
public record AlbumRs(Long id, String title, Integer year, ru.ulstu.is.server.api.artist.ArtistRs artist, Integer trackCount, String cover) {
 public static AlbumRs fromEntity(AlbumEntity e) { return new AlbumRs(e.getId(), e.getTitle(), e.getYear(), ru.ulstu.is.server.api.artist.ArtistRs.fromEntity(e.getArtist()), e.getTrackCount(), e.getCover()); }
}
