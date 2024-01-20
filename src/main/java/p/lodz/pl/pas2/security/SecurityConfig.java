package p.lodz.pl.pas2.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserAuthProvider userAuthProvider;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.
                addFilterBefore(new JwtAuthFilter(userAuthProvider), BasicAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((requests) -> { requests
                        .requestMatchers(HttpMethod.POST, "/api/v1/authentication/login").permitAll()
                        .requestMatchers("/api/v1/me/password").authenticated()
                        .requestMatchers("/api/v1/me/rent").hasRole("CLIENT")
                        .requestMatchers(HttpMethod.POST, "/api/v1/").hasRole("CLIENT")
                        .requestMatchers("/api/v1/administrators/**", "/api/v1/clients/**", "/api/v1/moderators**", "/api/v1/users/**").hasRole("ADMINISTRATOR")
                        .requestMatchers("/api/v1/movies/**", "/api/v1/rents/**").hasRole("MODERATOR");
                });


        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));

        return http.build();
    }
}
