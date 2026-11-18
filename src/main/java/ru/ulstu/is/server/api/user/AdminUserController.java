package ru.ulstu.is.server.api.user;

import java.util.List;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.ulstu.is.server.configuration.Constants;
import ru.ulstu.is.server.security.UserPrincipal;
import ru.ulstu.is.server.service.AdminUserService;

@RestController
@RequestMapping(Constants.API_URL + "/admin/users")
public class AdminUserController {
    private final AdminUserService service;

    public AdminUserController(AdminUserService service) { this.service = service; }

    @GetMapping
    public List<AdminUserRs> list() { return service.list(); }

    @PostMapping
    public AdminUserRs create(@RequestBody @Valid AdminUserRq dto) { return service.create(dto); }

    @PatchMapping("/{id}/block")
    public AdminUserRs block(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal current) {
        return service.setEnabled(id, false, current.getUsername());
    }

    @PatchMapping("/{id}/unblock")
    public AdminUserRs unblock(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal current) {
        return service.setEnabled(id, true, current.getUsername());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal current) {
        service.delete(id, current.getUsername());
        return ResponseEntity.noContent().build();
    }
}
