package ru.ulstu.is.server.api.artist;

import jakarta.validation.constraints.*;
public record ArtistRq(@NotBlank @Size(min=2,max=100) String name, @NotBlank String country, @NotNull @Min(1) Long genreId) {}
