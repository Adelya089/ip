package ru.ulstu.is.server.api.track;

import ru.ulstu.is.server.entity.TrackEntity;
public record TrackRs(Long id, String title, Integer duration, Long albumId) {
 public static TrackRs fromEntity(TrackEntity e) { return new TrackRs(e.getId(), e.getTitle(), e.getDuration(), e.getAlbum().getId()); }
}
