    package org.supplychain.supplychain.config;

    import lombok.RequiredArgsConstructor;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.authentication.AuthenticationProvider;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
    import org.springframework.security.web.SecurityFilterChain;
    import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
    import org.supplychain.supplychain.security.JwtAuthenticationFilter;

    import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

    @Configuration
    @EnableWebSecurity
    @RequiredArgsConstructor
    public class SecurityConfiguration {

        private final JwtAuthenticationFilter jwtAuthFilter;
        private final AuthenticationProvider authenticationProvider;

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
                            .requestMatchers("/api/suppliers/**").hasAnyRole(
                                    "ADMIN",
                                    "GESTIONNAIRE_APPROVISIONNEMENT",
                                    "RESPONSABLE_ACHATS",
                                    "SUPERVISEUR_LOGISTIQUE"
                            )

                            .requestMatchers("/api/raw-materials/**").hasAnyRole(
                                    "ADMIN",
                                    "GESTIONNAIRE_APPROVISIONNEMENT",
                                    "SUPERVISEUR_LOGISTIQUE"
                            )

                            .requestMatchers("/api/orders/**").hasAnyRole(
                                    "ADMIN",
                                    "RESPONSABLE_ACHATS",
                                    "SUPERVISEUR_LOGISTIQUE"
                            )

                            // ---------- PRODUCTION ----------
                            .requestMatchers("/api/products/**").hasAnyRole(
                                    "ADMIN",
                                    "CHEF_PRODUCTION",
                                    "SUPERVISEUR_PRODUCTION"
                            )

                            .requestMatchers("/api/production-orders/**").hasAnyRole(
                                    "ADMIN",
                                    "CHEF_PRODUCTION",
                                    "PLANIFICATEUR",
                                    "SUPERVISEUR_PRODUCTION"
                            )

                            // ---------- DELIVERY ----------
                            .requestMatchers("/api/customers/**").hasAnyRole(
                                    "ADMIN",
                                    "GESTIONNAIRE_COMMERCIAL"
                            )

                            .requestMatchers("/api/deliveries/**").hasAnyRole(
                                    "ADMIN",
                                    "RESPONSABLE_LOGISTIQUE",
                                    "SUPERVISEUR_LIVRAISONS"
                            )

                            .requestMatchers("/api/supplier-orders/**").hasAnyRole(
                                    "ADMIN",
                                    "RESPONSABLE_ACHATS"
                            )


                            .anyRequest().authenticated()
                    )
                    .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                    .authenticationProvider(authenticationProvider)
                    .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

            return http.build();
        }

    }