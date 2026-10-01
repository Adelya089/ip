package ru.ulstu.is.server.api.genre;

import jakarta.validation.constraints.NotBlank;

public record GenreRq(@NotBlank String name) {
}