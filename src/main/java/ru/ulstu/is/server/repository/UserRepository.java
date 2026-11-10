package ru.ulstu.is.server.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.ulstu.is.server.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByLoginIgnoreCase(String login);
}
