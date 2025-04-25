package pe.edu.vallegrande.database.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(auth -> auth
                        .pathMatchers("/swagger-ui.html", "/api/v1/person/active").permitAll()
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(Customizer.withDefaults())
                )
                .cors(cors -> cors
                        .configurationSource(exchange -> {
                            var config = new org.springframework.web.cors.CorsConfiguration();
                            config.setAllowCredentials(true);
                            // Especifica el origen permitido
                            config.addAllowedOrigin("http://localhost:4200"); // Cambia esto si tu frontend está en otro origen
                            config.addAllowedHeader("*");
                            config.addAllowedMethod("*");
                            return config;
                        })
                )
                .build();
    }
}