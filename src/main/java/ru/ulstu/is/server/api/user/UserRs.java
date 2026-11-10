package ru.ulstu.is.server.api.user;

import java.util.List;

import ru.ulstu.is.server.entity.UserEntity;

public record UserRs(String login, List<String> roles) {

    public static UserRs fromEntity(UserEntity entity) {
        return new UserRs(entity.getLogin(), List.of(entity.getRole().name()));
    }
}
