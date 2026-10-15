package ru.ulstu.is.server.entity;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name="artists")
public class ArtistEntity extends BaseEntity {
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String country;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="genre_id", nullable=false)
    private GenreEntity genre;
    @OneToMany(mappedBy="artist")
    List<AlbumEntity> albums = new ArrayList<>();
    public String getName() { return name; }
    public void setName(String value) { this.name = value; }
    public String getCountry() { return country; }
    public void setCountry(String value) { this.country = value; }
    public List<AlbumEntity> getAlbums() { return Collections.unmodifiableList(albums); }
    public void addAlbum(AlbumEntity value) { value.setArtist(this); }
    public void removeAlbum(AlbumEntity value) { if (albums.contains(value)) value.setArtist(null); }
    public GenreEntity getGenre() { return genre; }
    public void setGenre(GenreEntity value) {
        if (this.genre == value) return;
        if (this.genre != null) this.genre.artists.remove(this);
        this.genre = value;
        if (value != null && !value.artists.contains(this)) value.artists.add(this);
    }
}
