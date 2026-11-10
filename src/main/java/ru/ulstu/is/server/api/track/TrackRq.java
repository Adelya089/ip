package ru.ulstu.is.server.api.track;

import jakarta.validation.constraints.*;
public record  TrackRq(@NotBlank @Size(min=2,max=100) String title, @NotNull @Min(1) Integer duration, @NotNull @Min(1) Long albumId) {}
