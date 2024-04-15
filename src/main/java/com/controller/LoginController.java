package com.controller;

import com.entity.Login;
import com.repository.LoginRepository;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Optional;

@Controller
public class LoginController {

	@Autowired
	LoginRepository loginRepository;

	@Autowired
	LoginService loginService;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@GetMapping(value = "login")
	public String loginPage(Login login,Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
			return "redirect:/user";
		}
		model.addAttribute("login", login);
		return "login";
	}

	@GetMapping(value = "signup")
	public String signUpPage(Login login,Model model) {
		model.addAttribute("login", login);
		return "signup";
	}

	@PostMapping(value = "signupindb")
	public String signUpIntoDb(Login login) {
		login.setPassword(passwordEncoder.encode(login.getPassword()));
		String result = loginService.createAccount(login);
		System.out.println(result);
		return "signup";
	}

	@GetMapping(value = "changePasswordForm")
	public String change() {
		return "changePassword";
	}

	@PostMapping(value = "changePassword")
	public String changePassword(@RequestParam("oldPassword") String oldPassword,
		@RequestParam("newPassword") String newPassword,
		@RequestParam("confirmPassword") String confirmPassword,
		Principal principal, RedirectAttributes redirectAttributes) {
		if (!newPassword.equals(confirmPassword)) {
			redirectAttributes.addFlashAttribute("error", "New passwords do not match.");
			return "redirect:/changePassword";
		}

		String username = principal.getName();  // Get the username of the logged-in user
		Optional<Login> currentUserOptional = loginRepository.findUserByName(username);

		if (!currentUserOptional.isPresent()) {
			redirectAttributes.addFlashAttribute("error", "User not found.");
			return "redirect:/changePassword";
		}

		Login currentUser = currentUserOptional.get();

		if (!passwordEncoder.matches(oldPassword, currentUser.getPassword())) {
			redirectAttributes.addFlashAttribute("error", "Old password is incorrect.");
			return "redirect:/changePassword";
		}

		currentUser.setPassword(passwordEncoder.encode(newPassword));
		loginService.updateAccount(currentUser);
		redirectAttributes.addFlashAttribute("success", "Password changed successfully.");
		return "redirect:/login";
	}


}
