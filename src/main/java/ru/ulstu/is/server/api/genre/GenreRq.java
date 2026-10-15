package ru.ulstu.is.server.api.genre;

import jakarta.validation.constraints.*;
public record GenreRq(@NotBlank @Size(min=2,max=100) String name) {}
