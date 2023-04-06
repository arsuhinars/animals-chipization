package com.arsuhinars.animals_chipization.core.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(this::configureHttpRequests)
            .httpBasic()
            .and()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authenticationProvider(authenticationProvider());

        return http.build();
    }

    private void configureHttpRequests(
        AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorize
    ) {
        var chipperAuthority = AccountRoleAuthority.chipperAuthority().getAuthority();
        var adminAuthority = AccountRoleAuthority.adminAuthority().getAuthority();

        authorize
            .requestMatchers(HttpMethod.POST, "/registration").anonymous()
            .requestMatchers(HttpMethod.GET, "/status").permitAll()

            .requestMatchers(HttpMethod.GET, "/accounts/search").hasAuthority(adminAuthority)
            .requestMatchers(HttpMethod.POST, "/accounts").hasAuthority(adminAuthority)

            .requestMatchers(HttpMethod.POST, "/locations").hasAnyAuthority(adminAuthority, chipperAuthority)
            .requestMatchers(HttpMethod.PUT, "/locations/*").hasAnyAuthority(adminAuthority, chipperAuthority)
            .requestMatchers(HttpMethod.DELETE, "/locations/*").hasAuthority(adminAuthority)

            .requestMatchers(HttpMethod.POST, "/areas").hasAuthority(adminAuthority)
            .requestMatchers(HttpMethod.PUT, "/areas/*").hasAuthority(adminAuthority)
            .requestMatchers(HttpMethod.DELETE, "/areas/*").hasAuthority(adminAuthority)

            .requestMatchers(HttpMethod.POST, "/animals/types")
                .hasAnyAuthority(adminAuthority, chipperAuthority)
            .requestMatchers(HttpMethod.PUT, "/animals/types/*")
                .hasAnyAuthority(adminAuthority, chipperAuthority)
            .requestMatchers(HttpMethod.DELETE, "/animals/types/*")
                .hasAnyAuthority(adminAuthority)

            .requestMatchers(HttpMethod.POST, "/animals")
                .hasAnyAuthority(adminAuthority, chipperAuthority)
            .requestMatchers(HttpMethod.POST, "/animals/*/types/*")
                .hasAnyAuthority(adminAuthority, chipperAuthority)
            .requestMatchers(HttpMethod.PUT, "/animals/*")
                .hasAnyAuthority(adminAuthority, chipperAuthority)
            .requestMatchers(HttpMethod.PUT, "/animals/*/types")
                .hasAnyAuthority(adminAuthority, chipperAuthority)
            .requestMatchers(HttpMethod.DELETE, "/animals/*")
                .hasAnyAuthority(adminAuthority)
            .requestMatchers(HttpMethod.POST, "/animals/*/types/*")
                .hasAnyAuthority(adminAuthority, chipperAuthority)

            .requestMatchers(HttpMethod.POST, "/animals/*/locations/*")
                .hasAnyAuthority(adminAuthority, chipperAuthority)
            .requestMatchers(HttpMethod.PUT, "/animals/*/locations")
                .hasAnyAuthority(adminAuthority, chipperAuthority)
            .requestMatchers(HttpMethod.DELETE, "/animals/*/locations/*")
                .hasAuthority(adminAuthority)

            .anyRequest().authenticated();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        var authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
