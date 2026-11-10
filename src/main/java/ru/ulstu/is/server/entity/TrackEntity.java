package ru.ulstu.is.server.entity;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name="tracks")
public class TrackEntity extends BaseEntity {
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private Integer duration;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="album_id", nullable=false)
    private AlbumEntity album;
    public String getTitle() { return title; }
    public void setTitle(String value) { this.title = value; }
    public Integer getDuration() { return duration; }
    public void setDuration(Integer value) { this.duration = value; }
    public AlbumEntity getAlbum() { return album; }
    public void setAlbum(AlbumEntity value) {
        if (this.album == value) return;
        if (this.album != null) this.album.tracks.remove(this);
        this.album = value;
        if (value != null && !value.tracks.contains(this)) value.tracks.add(this);
    }
}
