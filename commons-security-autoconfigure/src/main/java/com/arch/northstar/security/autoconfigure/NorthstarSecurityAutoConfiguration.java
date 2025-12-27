package com.arch.northstar.security.autoconfigure;

import com.arch.northstar.security.auth.spi.NorthstarAuthenticationProvider;
import com.arch.northstar.security.autoconfigure.filter.*;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@AutoConfiguration
public class NorthstarSecurityAutoConfiguration {

    @Bean
    SecurityFilterChain northstarSecurityFilterChain(
            HttpSecurity http,
            List<NorthstarAuthenticationProvider> providers) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(
                        new NorthstarAuthenticationFilter(providers),
                        org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class
                )
                .addFilterAfter(
                        new SecurityContextCleanupFilter(),
                        NorthstarAuthenticationFilter.class
                );

        return http.build();
    }
}