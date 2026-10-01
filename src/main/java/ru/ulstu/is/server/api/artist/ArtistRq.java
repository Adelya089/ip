package ru.ulstu.is.server.api.artist;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ArtistRq(
        @NotBlank String name,
        @NotBlank String country,
        @NotNull @Min(1) Long genreId) {
}