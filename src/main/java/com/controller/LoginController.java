package com.controller;

import com.entity.Login;
import com.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

	@Autowired
	LoginService loginService;
	// http://localhost:8080/register , method : post 
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@GetMapping(value = "login")
	public String loginPage(Login login,Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
			return "redirect:/home";  // Redirect to a post-login page if already logged in
		}
		model.addAttribute("login", login);
		return "index1";
	}
	@GetMapping(value = "signup")
	public String signUpPage(Login login,Model model) {
		model.addAttribute("login", login);
		return "signup";
	}
	@PostMapping(value = "signupindb")
	public String signUpIntoDb(Login login, Model model) {
		login.setPassword(passwordEncoder.encode(login.getPassword()));
		String result = loginService.createAccount(login);
		System.out.println(result);
		return "signup";
	}
	
	
}
