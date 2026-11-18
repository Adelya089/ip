package ru.ulstu.is.server.api.user;

import ru.ulstu.is.server.entity.UserEntity;

public record AdminUserRs(Long id, String login, String role, boolean enabled) {
    public static AdminUserRs fromEntity(UserEntity user) {
        return new AdminUserRs(user.getId(), user.getLogin(), user.getRole().name(), user.isEnabled());
    }
}
