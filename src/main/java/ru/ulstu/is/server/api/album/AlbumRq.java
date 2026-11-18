package ru.ulstu.is.server.api.album;

import jakarta.validation.constraints.*;

public record AlbumRq(
        @NotBlank @Size(min = 2, max = 100) String title,
        @NotNull @Min(1980) Integer year,
        @NotNull @Min(1) Long artistId,
        @Size(max = 255) String cover
) {}
