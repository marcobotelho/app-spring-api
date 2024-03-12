package com.projeto.appspringapi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Autowired
        private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

        public static final String[] ENDPOINTS_WHITELIST = {
                        "/h2-console/**",
                        "/login",
                        "/usuarios/reset-senha/**"
        };

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http.authorizeHttpRequests(request -> request.requestMatchers(ENDPOINTS_WHITELIST).permitAll()
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .requestMatchers("/user/**").hasRole("USER")
                                .requestMatchers("/usuarios/**").hasAnyRole("ADMIN")
                                .requestMatchers("/clientes/**").hasAnyRole("ADMIN", "USER")
                                .requestMatchers("/telefones/**").hasAnyRole("ADMIN", "USER")
                                .anyRequest().authenticated())
                                .csrf(csrf -> csrf
                                                .ignoringRequestMatchers(ENDPOINTS_WHITELIST).disable())
                                .headers(headers -> headers.frameOptions(FrameOptionsConfig::disable))
                                .exceptionHandling(handling -> handling
                                                .authenticationEntryPoint(jwtAuthenticationEntryPoint))
                                .sessionManagement(management -> management
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

                http.addFilterBefore(jwtRequestFilter(), UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public JwtRequestFilter jwtRequestFilter() {
                return new JwtRequestFilter();
        }

        // @Bean
        // public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder)
        // {
        // UserDetails user = User.withUsername("user")
        // .password(passwordEncoder.encode("123"))
        // .roles("USER")
        // .build();

        // return new InMemoryUserDetailsManager(user);
        // }

}
