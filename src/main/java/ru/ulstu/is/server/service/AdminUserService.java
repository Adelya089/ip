package ru.ulstu.is.server.service;

import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ulstu.is.server.api.user.AdminUserRq;
import ru.ulstu.is.server.api.user.AdminUserRs;
import ru.ulstu.is.server.entity.UserEntity;
import ru.ulstu.is.server.entity.UserRole;
import ru.ulstu.is.server.error.InvalidValueException;
import ru.ulstu.is.server.error.NotFoundException;
import ru.ulstu.is.server.repository.UserRepository;

@Service
public class AdminUserService {
    private final UserRepository repository;
    private final PasswordEncoder encoder;

    public AdminUserService(UserRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    @Transactional(readOnly = true)
    public List<AdminUserRs> list() {
        return repository.findAll().stream()
                .sorted((a, b) -> a.getLogin().compareToIgnoreCase(b.getLogin()))
                .map(AdminUserRs::fromEntity).toList();
    }

    @Transactional
    public AdminUserRs create(AdminUserRq dto) {
        if (dto.role() == UserRole.ANONYMOUS) throw new InvalidValueException("Invalid role");
        if (repository.findByLoginIgnoreCase(dto.login()).isPresent())
            throw new InvalidValueException("login already exists");
        UserEntity user = new UserEntity();
        user.setLogin(dto.login());
        user.setPassword(encoder.encode(dto.password()));
        user.setRole(dto.role());
        user.setEnabled(true);
        return AdminUserRs.fromEntity(repository.save(user));
    }

    @Transactional
    public AdminUserRs setEnabled(Long id, boolean enabled, String currentLogin) {
        UserEntity user = find(id);
        if (!enabled && user.getLogin().equalsIgnoreCase(currentLogin))
            throw new InvalidValueException("Cannot block yourself");
        if (!enabled) ensureAnotherAdmin(user);
        user.setEnabled(enabled);
        return AdminUserRs.fromEntity(user);
    }

    @Transactional
    public void delete(Long id, String currentLogin) {
        UserEntity user = find(id);
        if (user.getLogin().equalsIgnoreCase(currentLogin))
            throw new InvalidValueException("Cannot delete yourself");
        ensureAnotherAdmin(user);
        repository.delete(user);
    }

    private UserEntity find(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("User", id));
    }

    private void ensureAnotherAdmin(UserEntity user) {
        if (user.getRole() == UserRole.ADMIN && user.isEnabled()
                && repository.countByRoleAndEnabledTrue(UserRole.ADMIN) <= 1)
            throw new InvalidValueException("Cannot block or delete the last active administrator");
    }
}
