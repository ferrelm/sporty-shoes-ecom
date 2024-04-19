package com.controller;

import com.entity.Orders;
import com.entity.Product;
import com.service.OrdersService;
import com.service.ProductService;
import com.service.UserDetailsManager;
import jakarta.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping("user")
public class UserController {

	@Autowired
	UserDetailsManager userDetailsManager;

	@Autowired
	ProductService productService;

	@Autowired
	OrdersService ordersService;

	@GetMapping(value = "")
	public String open(Model model, Product product, Orders order) {

		String name1="Store Product";
		String name2="Search Order";

		List<Product> listOfProduct = productService.findAllProducts();
		List<Object[]> orderdetails = productService.orderDetails();

		userDetailsManager.getAuthenticatedUser().ifPresent(order::setLogin);

		model.addAttribute("products", listOfProduct);
		model.addAttribute("buttonValue1", name1);
		model.addAttribute("buttonValue2", name2);
		model.addAttribute("product", product);

		userDetailsManager.getAuthenticatedUser().ifPresent(login -> {
			List<Object[]> orderDetailsByUser = productService.orderDetailsByUser(login.getUsername());
			model.addAttribute("orderdetailsbyuser", orderDetailsByUser);
			System.out.println(orderDetailsByUser);
		});

		System.out.println(listOfProduct);
		return "user";
	}

	@RequestMapping(value = "/placeOrder",method = RequestMethod.GET)
	public String placeOrder(Model model, HttpServletRequest req, Orders order,Product product) {
		int pid = Integer.parseInt(req.getParameter("pid"));
		order.setPid(pid);

		userDetailsManager.getAuthenticatedUser().ifPresent(order::setLogin);

		String name = "Store Product";
		String result = ordersService.placeOrder(order);
		List<Product> listOfProduct = productService.findAllProducts();
		model.addAttribute("products", listOfProduct);
		model.addAttribute("product", product);
		model.addAttribute("msg", result);
		model.addAttribute("buttonValue", name);

		userDetailsManager.getAuthenticatedUser().ifPresent(login -> {
			List<Object[]> orderDetailsByUser = productService.orderDetailsByUser(login.getUsername());
			model.addAttribute("orderdetailsbyuser", orderDetailsByUser);
		});

		return "user";
	}
}
