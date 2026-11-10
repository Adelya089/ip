package ru.ulstu.is.server.service;

import java.util.Objects;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ulstu.is.server.api.user.UserRq;
import ru.ulstu.is.server.api.user.UserRs;
import ru.ulstu.is.server.entity.UserEntity;
import ru.ulstu.is.server.entity.UserRole;
import ru.ulstu.is.server.repository.UserRepository;
import ru.ulstu.is.server.security.UserPrincipal;
import ru.ulstu.is.server.error.InvalidValueException;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserRs create(UserRq dto) {
        if (repository.findByLoginIgnoreCase(dto.login()).isPresent())
            throw new InvalidValueException("login already exists");
        if (!Objects.equals(dto.password(), dto.passwordConfirm()))
            throw new InvalidValueException("password confirmation");
        UserEntity user = new UserEntity();
        user.setLogin(dto.login());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setRole(UserRole.USER); // публичная регистрация не может выдать роль ADMIN
        return UserRs.fromEntity(repository.save(user));
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByLoginIgnoreCase(username)
                .map(UserPrincipal::new)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid login or password"));
    }

    @Transactional(readOnly = true)
    public UserPrincipal getCurrentUserByLogin(String login) {
        return repository.findByLoginIgnoreCase(login)
                .map(UserPrincipal::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
