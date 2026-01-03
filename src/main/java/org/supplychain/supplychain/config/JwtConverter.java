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
@Component
@RequiredArgsConstructor
public class JwtConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    private final JwtConverterProperties jwtConverterProperties;

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = Stream.concat(
                jwtGrantedAuthoritiesConverter.convert(jwt).stream(),
                extractAllRoles(jwt).stream()
        ).collect(Collectors.toSet());


        System.out.println("DEBUG: Claims found in Token: " + jwt.getClaims().keySet());
        System.out.println("DEBUG: Final Authorities passed to Spring: " +
                authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(", ")));
        // --------------------------------

        return new JwtAuthenticationToken(jwt, authorities, getPrincipalClaimName(jwt));
    }

    private Collection<? extends GrantedAuthority> extractAllRoles(Jwt jwt) {
        Set<String> roles = new HashSet<>();


        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        if (realmAccess != null && realmAccess.get("roles") instanceof Collection<?>) {
            roles.addAll((Collection<String>) realmAccess.get("roles"));


        }


        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        String clientId = jwtConverterProperties.getResourceId();

        if (resourceAccess != null && clientId != null) {
            Map<String, Object> clientAccess = (Map<String, Object>) resourceAccess.get(clientId);
            if (clientAccess != null && clientAccess.get("roles") instanceof Collection<?>) {
                roles.addAll((Collection<String>) clientAccess.get("roles"));

            }
        }

        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toSet());
    }

    private String getPrincipalClaimName(Jwt jwt) {
        String claimName = jwtConverterProperties.getPrincipalAttribute() != null ?
                jwtConverterProperties.getPrincipalAttribute() : "sub";
        return jwt.getClaim(claimName);
    }
}