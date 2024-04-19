package com.config;

import com.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurifyConfiguration {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

		return httpSecurity.
				csrf(csrf->csrf.disable())
            .authorizeHttpRequests(auth -> auth
							.requestMatchers("sporty-shoes", "home", "signup", "signupindb", "changePassword").permitAll()
							.requestMatchers("/user/**").hasRole("USER")
							.requestMatchers("/admin/**").hasRole("ADMIN")
							.anyRequest().authenticated())
					.formLogin(form->form.loginPage("/login")
							.successHandler(new SuccessHandlerApp())
							.permitAll()) // it open custom login page
					.logout(logout -> logout
							.logoutSuccessUrl("/login?logout")  // Redirect to login page with logout parameter
							.invalidateHttpSession(true)       // Invalidate session
							.clearAuthentication(true)         // Clear authentication
							.deleteCookies("JSESSIONID")       // Delete cookies
							.permitAll())
				.build();
	}

	@Autowired
	LoginService loginService;			// it is a type of UserDetailsService

	@Bean
	public UserDetailsService userDetailService() {
		return loginService;
	}

	// it is used to connect spring security with DAO layer
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider dap = new DaoAuthenticationProvider();
		dap.setUserDetailsService(loginService);
		dap.setPasswordEncoder(passwordEncoder());
		return dap;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}



