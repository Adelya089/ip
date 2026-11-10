package ru.ulstu.is.server.security;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.HandlerExceptionResolver;

import ru.ulstu.is.server.api.auth.AuthController;
import ru.ulstu.is.server.configuration.Constants;
import ru.ulstu.is.server.entity.UserRole;

@Configuration
@EnableMethodSecurity
public class SecurityConfiguration {
    public static final List<String> SPRINGDOCS_URLS = List.of(
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**");

    private final boolean isDev;

    public SecurityConfiguration(Environment env) {
        isDev = env.matchesProfiles("dev");
    }

    private Converter<Jwt, UsernamePasswordAuthenticationToken> jwtAuthenticationConverter(
            UserDetailsService userDetailsService) {
        return jwt -> {
            final String username = jwt.getSubject();
            final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            return new UsernamePasswordAuthenticationToken(
                    userDetails,
                    jwt,
                    userDetails.getAuthorities());
        };
    }

    @Bean
    SecurityFilterChain apiSecurity(
            HttpSecurity http,
            AuthenticationEntryPoint authenticationEntryPoint,
            UserDetailsService userDetailsService) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .formLogin(fl -> fl.disable())
                .logout(l -> l.disable())
                .httpBasic(hb -> hb.disable())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        if (isDev) {
            http
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/h2-console/**").hasRole(UserRole.ADMIN.name())
                            .requestMatchers(SPRINGDOCS_URLS.toArray(new String[0])).permitAll());
        }

        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(AuthController.URL + "/whoami").authenticated()
                        .requestMatchers(AuthController.URL + "/**").permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.POST, Constants.API_URL + "/**").hasRole(UserRole.ADMIN.name())
                        .requestMatchers(org.springframework.http.HttpMethod.PUT, Constants.API_URL + "/**").hasRole(UserRole.ADMIN.name())
                        .requestMatchers(org.springframework.http.HttpMethod.DELETE, Constants.API_URL + "/**").hasRole(UserRole.ADMIN.name())
                        .requestMatchers(Constants.API_URL + "/**").authenticated()
                        .anyRequest().denyAll());

        http.anonymous(a -> a.principal(UserPrincipal.anonymous()));

        http.oauth2ResourceServer(rs -> rs
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter(userDetailsService))));

        http.exceptionHandling(ex -> ex.authenticationEntryPoint(authenticationEntryPoint));

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(UserDetailsService users, PasswordEncoder encoder) {
        final DaoAuthenticationProvider provider = new DaoAuthenticationProvider(users);
        provider.setPasswordEncoder(encoder);
        return new ProviderManager(provider);
    }

    @Bean
    AuthenticationEntryPoint authenticationEntryPoint(
            @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        return (request, response, authException) -> resolver.resolveException(request, response, null, authException);
    }

    @Bean
    UrlBasedCorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(Constants.DEV_ORIGIN));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(Constants.API_URL + "/**", configuration);
        source.registerCorsConfiguration(AuthController.URL + "/**", configuration);
        return source;
    }
}
