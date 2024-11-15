package com.grytsyna.talkchat.configuration

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS

import groovy.transform.CompileStatic

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

import com.grytsyna.talkchat.service.SecUserService

/**
* SecurityConfiguration
*/
@CompileStatic
@Configuration
@EnableWebSecurity
class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter
    private final SecUserService secUserService

    @Autowired
    SecurityConfiguration(JwtAuthenticationFilter jwtAuthenticationFilter, SecUserService secUserService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter
        this.secUserService = secUserService
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests { request ->
                request
                    .requestMatchers('/status').permitAll()
                    .anyRequest().authenticated()
            }
            .sessionManagement { manager ->
                manager.sessionCreationPolicy(STATELESS)
            }
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter)

        return http.build()
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder()
    }

    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider()
        authProvider.userDetailsService = secUserService.userDetailsService()
        authProvider.passwordEncoder = passwordEncoder()
        return authProvider
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.authenticationManager
    }

}
