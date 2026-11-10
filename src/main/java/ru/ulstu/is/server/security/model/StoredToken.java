package ru.ulstu.is.server.security.model;

import java.time.Instant;

public record StoredToken(String username, String familyId, Instant expiresAt, Instant usedAt) {
}
