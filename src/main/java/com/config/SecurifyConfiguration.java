package com.config;

import com.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.function.Supplier;

@Configuration
@EnableWebSecurity
public class SecurifyConfiguration {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

		return httpSecurity.
				csrf(csrf->csrf.disable())
            .authorizeHttpRequests(auth -> auth
							.requestMatchers("everyone", "register").permitAll()
							.requestMatchers("signup").access(excludeAdmins())  // Assuming similar rules for "signup"
							.requestMatchers("signupindb").access(excludeAdmins())  // Adjusted to allow more flexible access
			.requestMatchers("/user/**").hasRole("USER")
			.requestMatchers("/admin/**").hasRole("ADMIN")
			.anyRequest().authenticated())
				//formLogin(form->form.permitAll()).		// it open pre-defined login page
				.formLogin(form->form.loginPage("/login").
						//successForwardUrl("/user").
						successHandler(new SuccessHandlerApp()).
						permitAll()) // it open custom login page
				.logout(logout -> logout
					.logoutSuccessUrl("/login?logout")  // Redirect to login page with logout parameter
					.invalidateHttpSession(true)       // Invalidate session
					.clearAuthentication(true)         // Clear authentication
					.deleteCookies("JSESSIONID")       // Delete cookies
					.permitAll())
			.build();
	}

	private AuthorizationManager<RequestAuthorizationContext> excludeAdmins() {
		return (Supplier<Authentication> authentication, RequestAuthorizationContext context) -> {
			if (!authentication.get().isAuthenticated()) {
				// Allow unauthenticated users
				return new AuthorizationDecision(true);
			} else {
				// Check if the authenticated user is ADMIN
				boolean isAdmin = authentication.get().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
				return new AuthorizationDecision(!isAdmin);
			}
		};
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



