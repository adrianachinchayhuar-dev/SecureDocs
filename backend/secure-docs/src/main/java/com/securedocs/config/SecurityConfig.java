package com.securedocs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // Desactivamos CSRF temporalmente
                .csrf(csrf -> csrf.disable())

                // Configuración de acceso
                .authorizeHttpRequests(auth -> auth

                        // Permitimos acceder al endpoint de prueba
                        .requestMatchers("/api/test").permitAll()

                        // Todo lo demás seguirá protegido
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}