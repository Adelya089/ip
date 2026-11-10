package ru.ulstu.is.server.service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import ru.ulstu.is.server.api.auth.LoginRq;
import ru.ulstu.is.server.error.InvalidValueException;
import ru.ulstu.is.server.security.JwtProperties;
import ru.ulstu.is.server.security.SecurityUtils;
import ru.ulstu.is.server.security.UserPrincipal;
import ru.ulstu.is.server.security.model.StoredToken;
import ru.ulstu.is.server.security.model.Tokens;

@Service
public class AuthService {
    private static final String REFRESH_TOKEN_ERROR = "refresh token";

    private final Map<String, StoredToken> tokenStorage = new ConcurrentHashMap<>();
    private final SecureRandom random = new SecureRandom();

    private final AuthenticationManager authenticationManager;
    private final JwtEncoder encoder;
    private final JwtProperties jwtProperties;
    private final UserService userService;

    public AuthService(
            AuthenticationManager authenticationManager,
            JwtEncoder encoder,
            JwtProperties jwtProperties,
            UserService userService) {
        this.authenticationManager = authenticationManager;
        this.encoder = encoder;
        this.jwtProperties = jwtProperties;
        this.userService = userService;
    }

    private String createAccessToken(String username, List<String> roles) {
        final Instant now = Instant.now();
        final JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(jwtProperties.getIssuer())
                .subject(username)
                .issuedAt(now)
                .expiresAt(now.plus(jwtProperties.getAccessTtl()))
                .claim(jwtProperties.getUserRoleCalm(), roles)
                .build();
        return encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    private String createRefreshToken(String username, String familyId) {
        final byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        final String raw = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        tokenStorage.put(
                SecurityUtils.sha256(raw),
                new StoredToken(username, familyId, Instant.now().plus(jwtProperties.getRefreshTtl()), null));
        return raw;
    }

    private String createRefreshToken(String username) {
        return createRefreshToken(username, UUID.randomUUID().toString());
    }

    private void revokeRefreshToken(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            return;
        }
        final String hash = SecurityUtils.sha256(refreshToken);
        tokenStorage.remove(hash);
    }

    private void revokeRefreshTokenFamily(String familyId) {
        tokenStorage.values().removeIf(token -> token.familyId().equals(familyId));
    }

    public Tokens login(LoginRq dto) {
        final Authentication auth = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(dto.login(), dto.password()));
        final String access = createAccessToken(auth.getName(), SecurityUtils.getRoles(auth.getAuthorities()));
        final String refresh = createRefreshToken(auth.getName());
        return new Tokens(access, refresh, jwtProperties.getAccessTtl().toSeconds());
    }

    public void logout(String refreshToken) {
        revokeRefreshToken(refreshToken);
    }

    private Tokens handleUsedToken(StoredToken stored, String refreshToken) {
        if (stored.usedAt().plusSeconds(10).isBefore(Instant.now())) {
            revokeRefreshTokenFamily(stored.familyId());
            throw new InvalidValueException(REFRESH_TOKEN_ERROR);
        }
        final UserPrincipal currentUser = userService.getCurrentUserByLogin(stored.username());
        final String access = createAccessToken(stored.username(),
                SecurityUtils.getRoles(currentUser.getAuthorities()));
        return new Tokens(access, refreshToken, jwtProperties.getAccessTtl().toSeconds());
    }

    private Tokens processRefresh(String hash, StoredToken stored) {
        tokenStorage.put(hash,
                new StoredToken(stored.username(), stored.familyId(), stored.expiresAt(), Instant.now()));

        final UserPrincipal currentUser = userService.getCurrentUserByLogin(stored.username());
        final String access = createAccessToken(stored.username(),
                SecurityUtils.getRoles(currentUser.getAuthorities()));
        final String refresh = createRefreshToken(stored.username(), stored.familyId());
        return new Tokens(access, refresh, jwtProperties.getAccessTtl().toSeconds());
    }

    public synchronized Tokens refresh(String refreshToken) {
        final String hash = SecurityUtils.sha256(refreshToken);
        final StoredToken stored = tokenStorage.get(hash);
        if (stored == null) {
            throw new InvalidValueException(REFRESH_TOKEN_ERROR);
        }
        if (stored.usedAt() != null) {
            return handleUsedToken(stored, refreshToken);
        }
        if (stored.expiresAt().isBefore(Instant.now())) {
            throw new InvalidValueException(REFRESH_TOKEN_ERROR);
        }
        return processRefresh(hash, stored);
    }
}
