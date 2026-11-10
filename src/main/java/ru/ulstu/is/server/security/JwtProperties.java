package ru.ulstu.is.server.security;

import java.time.Duration;
import java.util.UUID;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jwt", ignoreInvalidFields = true)
public class JwtProperties {
    private String issuer = UUID.randomUUID().toString();
    private Duration accessTtl = Duration.ofMinutes(15);
    private Duration refreshTtl = Duration.ofDays(14);
    private String userRoleCalm = "roles";

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public Duration getAccessTtl() {
        return accessTtl;
    }

    public void setAccessTtl(Duration accessTtl) {
        this.accessTtl = accessTtl;
    }

    public Duration getRefreshTtl() {
        return refreshTtl;
    }

    public void setRefreshTtl(Duration refreshTtl) {
        this.refreshTtl = refreshTtl;
    }

    public String getUserRoleCalm() {
        return userRoleCalm;
    }

    public void setUserRoleCalm(String userRoleCalm) {
        this.userRoleCalm = userRoleCalm;
    }
}
