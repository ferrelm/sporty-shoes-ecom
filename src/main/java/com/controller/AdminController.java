package com.controller;

import com.entity.Login;
import com.entity.Orders;
import com.entity.Product;
import com.exception.ProductNotFoundException;
import com.service.LoginService;
import com.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("admin")
public class AdminController {

	@Autowired
	LoginService loginService;

	@Autowired
	ProductService productService;

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
		if (product == null) {
			throw new ProductNotFoundException("Product with ID " + pid + " not found.");
		}

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

	@GetMapping(value = "/listUsers")
	public String listUsers(Model model) {

		List<Login> listOfUsers = loginService.findAllUsers();

		model.addAttribute("users", listOfUsers);

		System.out.println(listOfUsers);
		return "registeredUsers";
	}

	@GetMapping(value = "/searchUser")
	public String showSearchUserForm(Model model) {
		model.addAttribute("userSearch", new Login());  // Note: 'userSearch' used instead of 'users'
		model.addAttribute("buttonUser", "Search User");
		return "searchUser";  // name of the HTML file
	}

	@GetMapping(value = "/getUser")
	public String getUser(Model model, @RequestParam String username) {
		Optional<Login> userOptional = loginService.findByUsername(username);
		List<Login> users = new ArrayList<>();
		userOptional.ifPresent(users::add); // Only add if present

		model.addAttribute("users", users); // Pass list whether empty or containing a single user
		return "registeredUsers";
	}

	// Local exception handler within a controller
	@ExceptionHandler(RuntimeException.class)
	public String handleRuntimeException(RuntimeException ex, Model model) {
		model.addAttribute("errMsg", "Runtime error in Admin operations: " + ex.getMessage());
		return "adminErrorPage";  // Specific error page for admin controller
	}

	// In ControllerAdvice or Controller
	@ExceptionHandler(ProductNotFoundException.class)
	public String handleProductNotFoundException(ProductNotFoundException ex, Model model) {
		model.addAttribute("errMsg", ex.getMessage());
		return "productNotFoundPage";
	}
}
