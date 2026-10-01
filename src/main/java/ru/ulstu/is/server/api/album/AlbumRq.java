package ru.ulstu.is.server.api.album;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AlbumRq(
        @NotBlank String title,
        @NotNull @Min(1900) Integer year,
        @NotNull @Min(1) Long artistId,
        @NotNull @Min(1) Integer trackCount,
        String cover) {
}