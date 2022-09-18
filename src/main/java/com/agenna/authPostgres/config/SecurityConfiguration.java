package com.agenna.authPostgres.config;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfiguration {
    //@Value("${application.security.allowed-origins}")
    private List<String> allowedOrigins = Arrays.asList(
        "http://127.0.0.1:4200",
              "http://127.0.0.1:3000",
              "http://127.0.0.1:8080",
              "http://localhost:8080",
              "http://localhost:4200",
              "http://localhost:4201",
              "http://127.0.0.1:4201");
    //private List<String> allowedOrigins;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .cors().and()
            .authorizeHttpRequests((authz) -> authz
                .anyRequest().permitAll()
            )
            .httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        List<String> AllowedMethods = 
            Arrays.asList("HEAD", "GET", "POST",
                            "PUT", "PATCH", "DELETE");

        List<String> allowedHeaders = 
                    Arrays.asList("Authorization",
                                "Cache-Control",
                                "Content-Type");

        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(allowedOrigins);
        configuration.setAllowedMethods(AllowedMethods);
            // List.of("HEAD", "GET", "POST",
            //         "PUT", "PATCH", "DELETE"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(allowedHeaders);
            // List.of("Authorization",
            //         "Cache-Control",
            //         "Content-Type")); 
        final UrlBasedCorsConfigurationSource source=
            new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); 
        return source;              
    }
}
