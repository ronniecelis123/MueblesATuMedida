package com.mtm.mtm.config;

import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           JwtAuthenticationFilter jwtFilter) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/",
                                "/login",
                                "/registro",
                                "/auth/**",
                                "/css/**",
                                "/js/**",
                                "/libs/**",
                                "/models/**",
                                "/renders/**",
                                "/textures/**",
                                "/configurar/**",
                                "/calcular/**"
                        ).permitAll()

                        // GET API PUBLICOS
                        .requestMatchers(
                                HttpMethod.GET,
                                "/configuraciones/**"
                        ).permitAll()

                        .requestMatchers(
                                HttpMethod.GET,
                                "/productos/**"
                        ).permitAll()

                        .requestMatchers(
                                "/carrito/**",
                                "/mis-ordenes/**",
                                "/orden/**"
                        ).authenticated()

                        .requestMatchers("/admin/**")
                        .hasRole("ADMIN")

                        .anyRequest()
                        .authenticated()
                )

                .addFilterBefore(jwtFilter,
                        org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class)

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .deleteCookies("JWT")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .logoutSuccessUrl("/login?logout")
                )

                .formLogin(form -> form.disable());

        return http.build();
    }

}