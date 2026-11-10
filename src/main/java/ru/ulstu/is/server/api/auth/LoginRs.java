package ru.ulstu.is.server.api.auth;

import ru.ulstu.is.server.security.model.Tokens;

public record LoginRs(String accessToken, Long expiresIn) {
    public static LoginRs fromTokens(Tokens tokens) {
        return new LoginRs(tokens.accessToken(), tokens.expiresIn());
    }
}
