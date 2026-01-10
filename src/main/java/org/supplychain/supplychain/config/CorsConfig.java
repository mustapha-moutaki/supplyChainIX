package org.supplychain.supplychain.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // allow localhost:4200
        config.setAllowedOriginPatterns(List.of("http://localhost:4200"));

        // allow all HTTP methods
        config.setAllowedMethods(List.of("*"));

        // allow all headers
        config.setAllowedHeaders(List.of("*"));

        // JWT (no cookies)
        config.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
