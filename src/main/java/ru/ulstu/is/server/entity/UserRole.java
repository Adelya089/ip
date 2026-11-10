package ru.ulstu.is.server.entity;

import org.springframework.security.core.GrantedAuthority;

import ru.ulstu.is.server.configuration.Constants;

public enum UserRole implements GrantedAuthority {
    ANONYMOUS,
    ADMIN,
    USER;

    @Override
    public String getAuthority() {
        return Constants.USER_ROLE_PREFIX + this.name();
    }
}
