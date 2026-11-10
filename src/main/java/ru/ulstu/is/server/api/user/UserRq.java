package ru.ulstu.is.server.api.user;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
public record UserRq(
    @NotBlank @Size(min = 3,max = 20) String login,
    @NotBlank @Size(min = 6) String password,
    @NotBlank @Size(min = 6) String passwordConfirm) {}
