package ru.ulstu.is.server.api;

public class AlbumDto {
    private String id;
    private String title;
    private Integer year;
    private String artistId;
    private Integer trackCount;
    private String cover;

    public AlbumDto() {
    }

    public AlbumDto(String id, String title, Integer year, String artistId,
                    Integer trackCount, String cover) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.artistId = artistId;
        this.trackCount = trackCount;
        this.cover = cover;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public Integer getTrackCount() {
        return trackCount;
    }

    public void setTrackCount(Integer trackCount) {
        this.trackCount = trackCount;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }
}