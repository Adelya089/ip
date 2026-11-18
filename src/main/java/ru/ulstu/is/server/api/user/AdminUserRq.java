package ru.ulstu.is.server.api.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ru.ulstu.is.server.entity.UserRole;

public record AdminUserRq(
        @NotBlank @Size(min = 3, max = 20) String login,
        @NotBlank @Size(min = 6) String password,
        @NotNull UserRole role) {}
