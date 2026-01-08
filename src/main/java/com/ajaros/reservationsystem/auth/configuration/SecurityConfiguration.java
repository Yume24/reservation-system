package com.ajaros.reservationsystem.auth.configuration;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfiguration {
  private final JwtConfiguration jwtConfiguration;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) {
    sessionManagementConfig(http);
    csrfConfig(http);
    authorizeRequestsConfig(http);
    oauth2ResourceServerConfig(http);
    exceptionHandlingConfig(http);

    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) {
    return config.getAuthenticationManager();
  }

  private void sessionManagementConfig(HttpSecurity http) {
    http.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
  }

  private void csrfConfig(HttpSecurity http) {
    http.csrf(AbstractHttpConfigurer::disable);
  }

  private void authorizeRequestsConfig(HttpSecurity http) {
    http.authorizeHttpRequests(
        r ->
            r.requestMatchers(HttpMethod.POST, "/auth/**")
                .permitAll()
                .anyRequest()
                .authenticated());
  }

  private void oauth2ResourceServerConfig(HttpSecurity http) {
    http.oauth2ResourceServer(
        oauth2 ->
            oauth2.jwt(
                jwt ->
                    jwt.jwtAuthenticationConverter(jwtConfiguration.jwtAuthenticationConverter())));
  }

  private void exceptionHandlingConfig(HttpSecurity http) {
    http.exceptionHandling(
        c -> {
          c.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
          c.accessDeniedHandler(
              ((_, response, _) -> response.setStatus(HttpStatus.FORBIDDEN.value())));
        });
  }
}
