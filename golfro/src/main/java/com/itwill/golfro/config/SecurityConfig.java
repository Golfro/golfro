package com.itwill.golfro.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.itwill.golfro.service.UserDetailService;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

	@Autowired
	@Lazy
	private UserDetailService userService;

	@Bean
	@Lazy
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf((csrf) -> csrf.disable());

		http.userDetailsService(userService);

		http.formLogin(formLogin -> formLogin
			    .loginPage("/user/signin")
			    .defaultSuccessUrl("/", true)
			    .failureUrl("/user/signin?error=true")
			    .usernameParameter("userid") // 사용자 이름 필드 이름 확인
			    .passwordParameter("password")); // 비밀번호 필드 이름 확인


		http.authorizeHttpRequests((auth) -> auth.requestMatchers("/mainPost/create", "/post/details", "/post/modify",
				"/post/delete", "/post/update").authenticated().anyRequest().permitAll());

		return http.build();
	}
	
}