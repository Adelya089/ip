package ru.ulstu.is.server.entity;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name="albums")
public class AlbumEntity extends BaseEntity {
    @Column(nullable = false)
    private String title;
    @Column(name = "release_year", nullable = false)
    private Integer year;
    @Column(nullable = false)
    private Integer trackCount;
    @Column(nullable = true)
    private String cover;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="artist_id", nullable=false)
    private ArtistEntity artist;
    @OneToMany(mappedBy="album")
    List<TrackEntity> tracks = new ArrayList<>();
    public String getTitle() { return title; }
    public void setTitle(String value) { this.title = value; }
    public Integer getYear() { return year; }
    public void setYear(Integer value) { this.year = value; }
    public Integer getTrackCount() { return trackCount; }
    public void setTrackCount(Integer value) { this.trackCount = value; }
    public String getCover() { return cover; }
    public void setCover(String value) { this.cover = value; }
    public List<TrackEntity> getTracks() { return Collections.unmodifiableList(tracks); }
    public void addTrack(TrackEntity value) { value.setAlbum(this); }
    public void removeTrack(TrackEntity value) { if (tracks.contains(value)) value.setAlbum(null); }
    public ArtistEntity getArtist() { return artist; }
    public void setArtist(ArtistEntity value) {
        if (this.artist == value) return;
        if (this.artist != null) this.artist.albums.remove(this);
        this.artist = value;
        if (value != null && !value.albums.contains(this)) value.albums.add(this);
    }
}
