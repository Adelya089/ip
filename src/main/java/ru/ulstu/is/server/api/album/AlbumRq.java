package ru.ulstu.is.server.api.album;

import jakarta.validation.constraints.*;
public record AlbumRq(@NotBlank @Size(min=2,max=100) String title, @NotNull @Min(1980) @Max(2027) Integer year, @NotNull @Min(1) Long artistId, @NotNull @Min(1) @Max(100) Integer trackCount, String cover) {}
