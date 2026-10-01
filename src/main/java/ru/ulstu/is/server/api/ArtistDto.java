package ru.ulstu.is.server.api;

public class ArtistDto {
    private String id;
    private String name;
    private String country;
    private String genreId;

    public ArtistDto() {
    }

    public ArtistDto(String id, String name, String country, String genreId) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.genreId = genreId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getGenreId() {
        return genreId;
    }

    public void setGenreId(String genreId) {
        this.genreId = genreId;
    }
}