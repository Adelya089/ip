package ru.ulstu.is.server.api.auth;

import java.time.Duration;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import ru.ulstu.is.server.api.user.UserRq;
import ru.ulstu.is.server.api.user.UserRs;
import ru.ulstu.is.server.configuration.Constants;
import ru.ulstu.is.server.security.JwtProperties;
import ru.ulstu.is.server.security.SecurityUtils;
import ru.ulstu.is.server.security.UserPrincipal;
import ru.ulstu.is.server.service.AuthService;
import ru.ulstu.is.server.service.UserService;

@RestController
@RequestMapping(AuthController.URL)
public class AuthController {
    public static final String URL = "/auth";

    private final AuthService authService;
    private final UserService userService;
    private final JwtProperties jwtProperties;
    private final boolean secureCookies;

    public AuthController(AuthService authService, UserService userService, JwtProperties jwtProperties, org.springframework.core.env.Environment environment) {
        this.authService = authService;
        this.userService = userService;
        this.jwtProperties = jwtProperties;
        this.secureCookies = !environment.matchesProfiles("dev");
    }

    private ResponseCookie createRefreshTokenCookie(String token, Duration age) {
        return ResponseCookie.from("refreshToken", token)
                .httpOnly(true)
                .secure(secureCookies)
                .sameSite("Strict")
                .path(URL)
                .maxAge(age)
                .build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginRs> login(@RequestBody @Valid LoginRq dto) {
        final var tokens = authService.login(dto);
        final var cookie = createRefreshTokenCookie(tokens.refreshToken(), jwtProperties.getRefreshTtl());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(LoginRs.fromTokens(tokens));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @Parameter(hidden = true) @CookieValue(required = false) String refreshToken) {
        if (refreshToken != null) {
            authService.logout(refreshToken);
        }
        final var cookie = createRefreshTokenCookie("", Duration.ZERO);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginRs> refresh(
            @Parameter(hidden = true) @CookieValue(required = false) String refreshToken) {
        if (refreshToken == null) {
            return ResponseEntity.status(401).build();
        }
        final var tokens = authService.refresh(refreshToken);
        final var responseBuilder = ResponseEntity.ok();
        if (!tokens.refreshToken().equals(refreshToken)) {
            final var cookie = createRefreshTokenCookie(tokens.refreshToken(), jwtProperties.getRefreshTtl());
            responseBuilder.header(HttpHeaders.SET_COOKIE, cookie.toString());
        }
        return responseBuilder.body(LoginRs.fromTokens(tokens));
    }

    @PostMapping("/signup")
    public UserRs signup(@RequestBody @Valid UserRq dto) {
        return userService.create(dto);
    }

    @GetMapping(Constants.WHO_AM_I)
    public ResponseEntity<UserRs> whoami(@AuthenticationPrincipal UserPrincipal currentUser) {
        if (currentUser == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(
                new UserRs(currentUser.getUsername(), SecurityUtils.getRoles(currentUser.getAuthorities())));
    }
}
