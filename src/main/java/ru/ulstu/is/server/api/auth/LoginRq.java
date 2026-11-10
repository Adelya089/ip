package ru.ulstu.is.server.api.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRq(
        @NotBlank @Size(min = 3) String login,
        @NotBlank @Size(min = 3) String password) {
}
