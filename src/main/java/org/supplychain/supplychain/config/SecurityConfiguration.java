package org.supplychain.supplychain.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * PURPOSE:
 * This is the "Security Guard" of the application. It defines the global security
 * rules, such as which URLs are public, which require roles, and how to validate
 * the incoming Keycloak JWT tokens.
 *
 * HOW IT WORKS:
 * It configures Spring Security to act as an OAuth2 Resource Server. This means
 * the app won't have its own login page; instead, it expects a "Bearer Token"
 * in the header of every request.
 */
@Configuration
@EnableWebSecurity // Enables Spring Security's web support
@RequiredArgsConstructor
public class SecurityConfiguration {

    // Our custom converter that extracts roles (like ADMIN) from the Keycloak token
    private final JwtConverter jwtConverter;

    // The URL of the Keycloak server used to fetch public keys to verify token signatures
    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwkSetUri;

    /**
     * The SecurityFilterChain defines the "checkpoints" for every HTTP request.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF because we are using JWT tokens (Stateless), not Cookies
                .csrf(AbstractHttpConfigurer::disable)

                // Set session policy to STATELESS: Spring won't create or use HTTP sessions (no cookies)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(req -> req
                        // PUBLIC: Allow anyone to access Auth, Swagger documentation, and UI
                        .requestMatchers("/api/auth/**", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                        // PROTECTED: Role-Based Access Control (RBAC)
                        // Note: .hasAnyRole automatically looks for the "ROLE_" prefix in the authorities
                        .requestMatchers("/api/suppliers/**").hasAnyRole("ADMIN", "GESTIONNAIRE_APPROVISIONNEMENT")
                        .requestMatchers("/api/products/**").hasAnyRole("ADMIN", "CHEF_PRODUCTION")

                        // SECURE BY DEFAULT: Any other request not mentioned above must be authenticated
                        .anyRequest().authenticated()
                )

                // Configure the app to behave as an OAuth2 Resource Server
                .oauth2ResourceServer(oauth2 -> oauth2
                        // Use our custom jwtConverter to transform JWT claims into Spring Authorities
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtConverter))
                );

        return http.build();
    }

    /**
     * The JwtDecoder is responsible for checking if the token is valid.
     * It fetches the Public Keys from Keycloak using the jwkSetUri.
     */
    @Bean
    public JwtDecoder jwtDecoder() {
        // NimbusJwtDecoder is the standard implementation.
        // withJwkSetUri tells it where to find the keys to verify the RS256 signature.
        return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }
}