package org.supplychain.supplychain.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    // Note: Removed JwtAuthenticationFilter and AuthenticationProvider to stop the JJWT error

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req -> req
                        // ---------- AUTH & SWAGGER (PUBLIC) ----------
                        .requestMatchers(
                                "/api/auth/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        // ---------- APPROVISIONNEMENT ----------
                        .requestMatchers("/api/suppliers/**").hasAnyRole("ADMIN", "GESTIONNAIRE_APPROVISIONNEMENT", "RESPONSABLE_ACHATS", "SUPERVISEUR_LOGISTIQUE")
                        .requestMatchers("/api/raw-materials/**").hasAnyRole("ADMIN", "GESTIONNAIRE_APPROVISIONNEMENT", "SUPERVISEUR_LOGISTIQUE")
                        .requestMatchers("/api/orders/**").hasAnyRole("ADMIN", "RESPONSABLE_ACHATS", "SUPERVISEUR_LOGISTIQUE")

                        // ---------- PRODUCTION ----------
                        .requestMatchers("/api/products/**").hasAnyRole("ADMIN", "CHEF_PRODUCTION", "SUPERVISEUR_PRODUCTION")
                        .requestMatchers("/api/production-orders/**").hasAnyRole("ADMIN", "CHEF_PRODUCTION", "PLANIFICATEUR", "SUPERVISEUR_PRODUCTION")

                        // ---------- DELIVERY ----------
                        .requestMatchers("/api/customers/**").hasAnyRole("ADMIN", "GESTIONNAIRE_COMMERCIAL")
                        .requestMatchers("/api/deliveries/**").hasAnyRole("ADMIN", "RESPONSABLE_LOGISTIQUE", "SUPERVISEUR_LIVRAISONS")
                        .requestMatchers("/api/supplier-orders/**").hasAnyRole("ADMIN", "RESPONSABLE_ACHATS")

                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                // This replaces your manual JwtAuthenticationFilter
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                );

        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri(
                "http://localhost:8081/realms/springboot/protocol/openid-connect/certs"
        ).build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Map<String, Object> realmAccess = jwt.getClaim("realm_access");
            if (realmAccess == null || realmAccess.get("roles") == null) {
                return Collections.emptyList();
            }
            Collection<String> roles = (Collection<String>) realmAccess.get("roles");
            return roles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role)) // MUST have ROLE_ prefix
                    .collect(Collectors.toList());
        });
        return converter;
    }
}