package com.springprojects.realtimechatapp.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {


//	@Bean
//	public PasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
//	}

	@Bean
	public UserDetailsManager userDetailsManager(DataSource dataSource) {
		JdbcUserDetailsManager theUserDetailsManager = new JdbcUserDetailsManager(dataSource);
		theUserDetailsManager
				.setUsersByUsernameQuery("select user_email, user_password, enabled from chat_user where user_email=?");

		theUserDetailsManager.setAuthoritiesByUsernameQuery("select user_email, authority from authorities where user_email=?");
		return theUserDetailsManager;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(configurer -> configurer.anyRequest().authenticated())
				.formLogin(form -> form.loginPage("/showLoginForm")
						.loginProcessingUrl("/authenticateTheUser")
						.usernameParameter("user_email")
						.passwordParameter("user_password")
						.defaultSuccessUrl("/showChatPage", true)
						.permitAll());
		return http.build();
	}

	// Solution: By default, Spring Security expects the username and password
	// parameters in the login request to be named username and password,
	// respectively. If your form input names are different, you need to specify
	// them in your security configuration.
}