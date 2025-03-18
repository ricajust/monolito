package com.edugo.edugo_tcc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.edugo.edugo_tcc.config.filters.JwtAuthenticationFilter;
import com.edugo.edugo_tcc.service.impl.AutenticacaoService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private AutenticacaoService autenticacaoService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf((csrf) -> csrf.disable())
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // JWT é stateless
                )
                .authorizeHttpRequests((authorize) -> authorize
                    .requestMatchers(new AntPathRequestMatcher("/auth/**")).permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/swagger-ui/**")).permitAll() // Permite acesso ao Swagger UI
                    .requestMatchers(new AntPathRequestMatcher("/v3/api-docs/**")).permitAll() // Permite acesso à documentação OpenAPI
                    .requestMatchers(new AntPathRequestMatcher("/webjars/**")).permitAll() // Permite acesso aos recursos do Swagger UI
                    .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // Adiciona o filtro JWT
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(autenticacaoService); // Use sua implementação de UserDetailsService
        authProvider.setPasswordEncoder(passwordEncoder()); // Use o PasswordEncoder configurado
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}