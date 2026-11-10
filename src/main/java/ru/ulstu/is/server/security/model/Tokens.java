package ru.ulstu.is.server.security.model;

public record Tokens(String accessToken, String refreshToken, Long expiresIn) {
}
