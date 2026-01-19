package org.greatsokol.springsecurityjpa.security;

import lombok.RequiredArgsConstructor;
import org.greatsokol.springsecurityjpa.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfiguration {
    //private final UserService userService;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(//AuthenticationConfiguration configuration,
                                                UserService userDetailsService,
                                                PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        //return configuration.getAuthenticationManager();
        return new ProviderManager(provider);
    }
//
//    @Bean
//    DaoAuthenticationProvider daoAuthenticationProvider(UserService userDetailsService, PasswordEncoder passwordEncoder) {
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
//        provider.setPasswordEncoder(passwordEncoder);
//        return provider;
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, TokenFilter tokenFilter) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)

                .cors(corsConfigurer ->
                        corsConfigurer.configurationSource(
                                request -> new CorsConfiguration().applyPermitDefaultValues()))

                .exceptionHandling(exceptions ->
                        exceptions.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests((authorize) ->
                        authorize
                                .requestMatchers("/api/auth/**", "/error", "/actuator/**").permitAll()
                                .anyRequest().authenticated()
                )

                .addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class)
        ;


        return http.build();
    }
}

