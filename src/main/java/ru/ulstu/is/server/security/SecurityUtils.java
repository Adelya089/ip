package ru.ulstu.is.server.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.HexFormat;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import ru.ulstu.is.server.configuration.Constants;

public class SecurityUtils {

    private SecurityUtils() {
    }

    public static String sha256(String rawToken) {
        try {
            byte[] hash = MessageDigest.getInstance("SHA-256").digest(rawToken.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    public static List<String> getRoles(Collection<? extends GrantedAuthority> auths) {
        return auths.stream()
                .map(GrantedAuthority::getAuthority)
                .filter(item -> item.startsWith(Constants.USER_ROLE_PREFIX))
                .map(item -> item.substring(Constants.USER_ROLE_PREFIX.length()))
                .toList();
    }

}
