package com.controller;

import com.entity.Login;
import com.entity.Orders;
import com.entity.Product;
import com.service.LoginService;
import com.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("admin")
public class AdminController {

	@Autowired
	LoginService loginService;

	@Autowired
	ProductService productService;

	@GetMapping(value = "/getUsers")
	public String open(Model model) {

		List<Login> listOfUsers = loginService.findAllUsers();

		model.addAttribute("users", listOfUsers);

		System.out.println(listOfUsers);
		return "registeredUsers";
	}

	@GetMapping(value = "")
	public String open(Model model, Product product) {

		List<Product> listOfProduct = productService.findAllProducts();
		List<Object[]> orderdetails = productService.orderDetails();
		List<Login> listOfUsers = loginService.findAllUsers();

		model.addAttribute("products", listOfProduct);
		model.addAttribute("product", product);
		model.addAttribute("orderdetails", orderdetails);
		model.addAttribute("users", listOfUsers);
		model.addAttribute("buttonProduct", "Store Product");
		model.addAttribute("buttonOrder", "Search Order");

		System.out.println(listOfProduct);
		return "admin";
	}

	@GetMapping(value = "/listOrders")
	public String listOrders(Model model, Orders order, @RequestParam String category,
		@RequestParam String startDate,
		@RequestParam String endDate) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
		LocalDateTime startDateTime = LocalDateTime.parse(startDate, formatter);
		LocalDateTime endDateTime = LocalDateTime.parse(endDate, formatter);

		List<Product> listOfProduct = productService.findAllProducts();
		List<Object[]> orderdetails = productService.listOrders(category, startDateTime, endDateTime);
		List<Login> listOfUsers = loginService.findAllUsers();

		model.addAttribute("product", new Product());
		model.addAttribute("products", listOfProduct);
		model.addAttribute("orderdetails", orderdetails);
		model.addAttribute("users", listOfUsers);
		model.addAttribute("buttonProduct", "Store Product");
		model.addAttribute("buttonOrder", "Search Order");

		System.out.println(orderdetails);
		return "admin"; // Ensure "admin" is the correct Thymeleaf template name
	}

	@PostMapping(value = "/addProduct")
	public String addProductDetails(Model model, Product product, HttpServletRequest req) {
		String b1 = req.getParameter("b1");
		String result="";
		if(b1.equals("Store Product")) {
			result = productService.storeProduct(product);
		}else {
			result = productService.updateProduct(product);
		}
		product.setPid(0);
		product.setPname("");
		product.setPrice(0);
		product.setCategory("");

		List<Product> listOfProduct = productService.findAllProducts();
		List<Object[]> orderdetails = productService.orderDetails();
		List<Login> listOfUsers = loginService.findAllUsers();

		model.addAttribute("product", product);
		model.addAttribute("products", listOfProduct);
		model.addAttribute("orderdetails", orderdetails);
		model.addAttribute("msg", result);
		model.addAttribute("users", listOfUsers);
		model.addAttribute("buttonProduct", "Store Product");
		model.addAttribute("buttonOrder", "Search Order");

		return "admin";
	}

	@GetMapping(value = "/updateProduct")
	public String searchProductById(Model model, HttpServletRequest req) {
		int pid = Integer.parseInt(req.getParameter("pid"));

		Product product = productService.searchProductById(pid);
		List<Product> listOfProduct = productService.findAllProducts();
		List<Object[]> orderdetails = productService.orderDetails();
		List<Login> listOfUsers = loginService.findAllUsers();

		model.addAttribute("product", product);
		model.addAttribute("products", listOfProduct);
		model.addAttribute("orderdetails", orderdetails);
		model.addAttribute("users", listOfUsers);
		model.addAttribute("buttonProduct", "Update Product");
		model.addAttribute("buttonOrder", "Search Order");

		return "admin";
	}

	@GetMapping(value = "/deleteProduct")
		public String deleteProductById(Model model, Product product,HttpServletRequest req) {
		int pid = Integer.parseInt(req.getParameter("pid"));
		System.out.println("pid is "+pid);

		String result = productService.deleteProduct(pid);
		List<Product> listOfProduct = productService.findAllProducts();
		List<Object[]> orderdetails = productService.orderDetails();
		List<Login> listOfUsers = loginService.findAllUsers();

		model.addAttribute("product", product);
		model.addAttribute("products", listOfProduct);
		model.addAttribute("orderdetails", orderdetails);
		model.addAttribute("msg", result);
		model.addAttribute("users", listOfUsers);
		model.addAttribute("buttonProduct", "Store Product");
		model.addAttribute("buttonOrder", "Search Order");

		return "admin";
	}

}
