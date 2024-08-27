package com.itwill.golfro.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// import com.itwill.golfro.service.UserDetailService;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

	@Autowired
	// private UserDetailService userService;

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf((csrf) -> csrf.disable());

		// http.userDetailsService(userService);

		http.formLogin((login) -> login.loginPage("/user/signin").defaultSuccessUrl("/", true)
				.failureUrl("/user/signin?error=true"));

		http.authorizeHttpRequests((auth) -> auth.requestMatchers("/post/create", "/post/details", "/post/modify",
				"/post/delete", "/post/update", "/api/comment/**").authenticated().anyRequest().permitAll());

		return http.build();
	}
	
}