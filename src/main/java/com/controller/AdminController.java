package com.controller;

import com.entity.Login;
import com.entity.Product;
import com.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
//@RequestMapping("admin")
public class AdminController {

/*	@GetMapping(value = "/admin/getUsers")
	public String open() {
		return "admin0";
	}*/

	@Autowired
	LoginService loginService;

	@GetMapping(value = "/admin/getUsers")
	public String open(Model model) {

		List<Login> listOfUsers = loginService.findAllUsers();

		model.addAttribute("users", listOfUsers);

		System.out.println(listOfUsers);
		return "admin0";
	}


}
