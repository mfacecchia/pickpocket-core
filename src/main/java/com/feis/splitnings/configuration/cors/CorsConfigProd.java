package com.feis.splitnings.configuration.cors;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Profile("prod")
@Configuration
public class CorsConfigProd {

    @Bean
    public UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        // TODO: Configure with prod hostname
        corsConfiguration.setAllowedOrigins(List.of());

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }
}
