package org.supplychain.supplychain.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class CustomOidcUserService extends OidcUserService {

    private final JwtDecoder jwtDecoder;

    public CustomOidcUserService(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);
        Set<GrantedAuthority> mappedAuthorities = new HashSet<>(oidcUser.getAuthorities());

        String tokenValue = userRequest.getAccessToken().getTokenValue();
        Jwt jwt = jwtDecoder.decode(tokenValue);

        // 1. Map Realm Roles
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        if (realmAccess != null && realmAccess.get("roles") instanceof Collection<?> roles) {
            mappedAuthorities.addAll(roles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toString())) // FIXED: Added ROLE_
                    .collect(Collectors.toList()));
        }

        // 2. Map Resource (Client) Roles
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        if (resourceAccess != null) {
            resourceAccess.forEach((client, accessObj) -> {
                Map<String, Object> clientAccess = (Map<String, Object>) accessObj;
                if (clientAccess.get("roles") instanceof Collection<?> clientRoles) {
                    mappedAuthorities.addAll(clientRoles.stream()
                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toString())) // FIXED: Added ROLE_
                            .collect(Collectors.toList()));
                }
            });
        }

        return new DefaultOidcUser(
                mappedAuthorities,
                oidcUser.getIdToken(),
                oidcUser.getUserInfo(),
                "preferred_username"
        );
    }
}