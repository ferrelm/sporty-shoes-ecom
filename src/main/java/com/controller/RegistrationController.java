package com.controller;

import com.entity.Login;
import com.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {

	@Autowired
	LoginService loginService;
	// http://localhost:8080/register , method : post 
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@PostMapping(value = "register",consumes =  MediaType.APPLICATION_JSON_VALUE)
	public String register(@RequestBody Login login) {
		login.setPassword(passwordEncoder.encode(login.getPassword()));
		return loginService.createAccount(login);
		
	}
}
