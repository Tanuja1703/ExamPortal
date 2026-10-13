package com.smartportal.SmartOnlineExamPortal;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.smartportal.SmartOnlineExamPortal.service.CustomUserDetailsService;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(
            CustomUserDetailsService userDetailsService,
            BCryptPasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            DaoAuthenticationProvider authProvider) throws Exception {

        http
            // CSRF OFF for forms + file upload
            .csrf(csrf -> csrf.disable())
            .authenticationProvider(authProvider)
            .authorizeHttpRequests(auth -> auth
                // PUBLIC URLs
                .requestMatchers(
                    "/", "/login", "/register","/forgot",
                    "/admin/**",       // admin panel open without login
                    "/courses/**",
                    "/course/**",
                    "/uploads/**",
                    "/css/**", "/js/**", "/images/**"
                ).permitAll()
                // USER URLs require authentication
                .requestMatchers("/profile/**", "/edit-profile/**").authenticated()
                .anyRequest().permitAll()
            )
            // Enable login
            .formLogin(form -> form
    .loginPage("/login")
    .loginProcessingUrl("/login")
    .usernameParameter("email")    // <-- add this
    .passwordParameter("password")
    .defaultSuccessUrl("/redirect-after-login", true)
    .permitAll()
)

            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );

        return http.build();
    }
}
