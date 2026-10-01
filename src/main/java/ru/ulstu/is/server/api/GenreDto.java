package ru.ulstu.is.server.api;

public class GenreDto {
    private String id;
    private String name;

    public GenreDto() {
    }

    public GenreDto(String id, String name) {
        this.id = id;
        this.name = name;
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
}