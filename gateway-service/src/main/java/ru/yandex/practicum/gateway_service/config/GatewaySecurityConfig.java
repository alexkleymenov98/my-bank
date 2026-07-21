package ru.yandex.practicum.gateway_service.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
//import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.util.*;

@Configuration
@EnableWebSecurity
@Slf4j
public class GatewaySecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                );

        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            log.info("========== JWT TOKEN RECEIVED ==========");
            log.info("JWT Subject: {}", jwt.getSubject());
            log.info("realm_access: {}", jwt.getClaimAsMap("realm_access"));

            Set<GrantedAuthority> authorities = new HashSet<>();

            // 1. Извлекаем scope (стандартные SCOPE_*)
            JwtGrantedAuthoritiesConverter scopeConverter = new JwtGrantedAuthoritiesConverter();
            scopeConverter.setAuthorityPrefix("SCOPE_");
            authorities.addAll(scopeConverter.convert(jwt));

            // 2. Извлекаем роли из realm_access (это то, что вы пропустили!)
            Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
            if (realmAccess != null && realmAccess.containsKey("roles")) {
                List<String> roles = (List<String>) realmAccess.get("roles");
                log.info("Roles from realm_access: {}", roles);

                roles.forEach(role -> {
                    // Добавляем как ROLE_* для hasRole()
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
                    // Добавляем как SCOPE_* для hasAuthority()
                    authorities.add(new SimpleGrantedAuthority("SCOPE_" + role));
                });
            }

            log.info("Extracted Authorities: {}", authorities);
            log.info("=======================================");

            return authorities;
        });
        return converter;
    }
}
