package ru.ulstu.is.server.entity;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name="genres")
public class GenreEntity extends BaseEntity {
    @Column(nullable = false)
    private String name;
    @OneToMany(mappedBy="genre")
    List<ArtistEntity> artists = new ArrayList<>();
    public String getName() { return name; }
    public void setName(String value) { this.name = value; }
    public List<ArtistEntity> getArtists() { return Collections.unmodifiableList(artists); }
    public void addArtist(ArtistEntity value) { value.setGenre(this); }
    public void removeArtist(ArtistEntity value) { if (artists.contains(value)) value.setGenre(null); }
}
