package ru.ulstu.is.server.security;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import ru.ulstu.is.server.configuration.Constants;

@Configuration
public class JwtConfiguration {
    private final JwtProperties jwtProperties;

    public JwtConfiguration(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @Bean
    KeyPair jwtSigningKey() throws NoSuchAlgorithmException {
        final KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        return generator.generateKeyPair();
    }

    @Bean
    JwtEncoder jwtEncoder(KeyPair key) {
        return NimbusJwtEncoder
                .withKeyPair((RSAPublicKey) key.getPublic(), (RSAPrivateKey) key.getPrivate())
                .build();
    }

    @Bean
    JwtDecoder jwtDecoder(KeyPair key) {
        final NimbusJwtDecoder decoder = NimbusJwtDecoder.withPublicKey((RSAPublicKey) key.getPublic()).build();
        decoder.setJwtValidator(JwtValidators.createDefaultWithIssuer(jwtProperties.getIssuer()));
        return decoder;
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        final JwtGrantedAuthoritiesConverter roles = new JwtGrantedAuthoritiesConverter();
        roles.setAuthoritiesClaimName(jwtProperties.getUserRoleCalm());
        roles.setAuthorityPrefix(Constants.USER_ROLE_PREFIX);
        final JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(roles);
        return converter;
    }
}
