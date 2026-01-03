package org.supplychain.supplychain.config;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class acts as a "Bridge."
 * It converts the raw JWT (JSON Web Token) sent by Keycloak into a
 * Spring Security "Authentication" object that the application can understand.
 */
@Component
@RequiredArgsConstructor
public class JwtConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    // This is a built-in Spring class that automatically extracts "Scopes" (like SCOPE_email, SCOPE_profile)
    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    // This holds our custom settings from application.yml (like the Client ID)
    private final JwtConverterProperties jwtConverterProperties;

    /**
     * The main conversion method required by the Converter interface.
     * @param jwt The raw decrypted token received in the HTTP Header.
     * @return A fully populated Authentication token for Spring Security.
     */
    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        // Stream.concat joins two lists together:
        // 1. Standard Scopes (SCOPE_...)
        // 2. Our custom Keycloak Roles (ROLE_...)
        Collection<GrantedAuthority> authorities = Stream.concat(
                jwtGrantedAuthoritiesConverter.convert(jwt).stream(), // Extract default scopes
                extractAllRoles(jwt).stream()                         // Extract custom Keycloak roles
        ).collect(Collectors.toSet()); // Convert the combined stream into a unique Set

        // Debugging lines to see exactly what is happening inside the logs
        System.out.println("DEBUG: Claims found in Token: " + jwt.getClaims().keySet());
        System.out.println("DEBUG: Final Authorities passed to Spring: " +
                authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(", ")));

        // Create the final object Spring uses. It contains:
        // - The JWT itself
        // - The list of permissions (Authorities)
        // - The Username (Principal)
        return new JwtAuthenticationToken(jwt, authorities, getPrincipalClaimName(jwt));
    }

    /**
     * Custom method to dig into the Keycloak JSON structure and find user roles.
     */
    private Collection<? extends GrantedAuthority> extractAllRoles(Jwt jwt) {
        Set<String> roles = new HashSet<>();

        // 1. Get "Realm Roles" (Roles that apply to the whole Keycloak environment)
        // jwt.getClaim fetches a specific field from the Token's JSON payload
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");

        // instanceof check: ensures the data we found is actually a list of roles
        if (realmAccess != null && realmAccess.get("roles") instanceof Collection<?>) {
            roles.addAll((Collection<String>) realmAccess.get("roles"));
        }

        // 2. Get "Client Roles" (Roles specific to this microservice, e.g., springboot-client)
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        String clientId = jwtConverterProperties.getResourceId();

        if (resourceAccess != null && clientId != null) {
            Map<String, Object> clientAccess = (Map<String, Object>) resourceAccess.get(clientId);
            if (clientAccess != null && clientAccess.get("roles") instanceof Collection<?>) {
                roles.addAll((Collection<String>) clientAccess.get("roles"));
            }
        }

        // Spring Security expects roles to start with "ROLE_" (e.g., ROLE_ADMIN)
        // .map converts each plain string (e.g., "ADMIN") into a SimpleGrantedAuthority object
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toSet());
    }

    /**
     * Determines which field in the token should be used as the "Username."
     */
    private String getPrincipalClaimName(Jwt jwt) {
        // We check if "principalAttribute" is set in application.yml (e.g., preferred_username)
        // If not, we default to "sub" (the unique ID of the user)
        String claimName = jwtConverterProperties.getPrincipalAttribute() != null ?
                jwtConverterProperties.getPrincipalAttribute() : "sub";

        return jwt.getClaim(claimName);
    }
}