package dev.test.configuration;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


@EnableWebSecurity
@EnableAutoConfiguration
public class SpringSecurityConfig {

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http.csrf().disable()
        .authorizeRequests(
            auth -> auth.anyRequest().authenticated()).httpBasic().and()
        .formLogin(Customizer.withDefaults()).build();
  }

  @Bean
  InMemoryUserDetailsManager userDetailsManager() {
    return new InMemoryUserDetailsManager(User.withUsername("user")
        .password("{noop}password")
        .roles("USER")
        .build()
    );
  }

}
