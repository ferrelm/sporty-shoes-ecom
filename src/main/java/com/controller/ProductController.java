package com.controller;

import com.entity.Orders;
import com.entity.Product;
import com.service.OrdersService;
import com.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
//@RequestMapping("admin")
public class ProductController {

	@Autowired
	ProductService productService;
	
	@Autowired
	OrdersService ordersService;
	
	//@RequestMapping(value = "/",method = RequestMethod.GET)
	@GetMapping(value = "admin")
	public String open(Model model, Product product) {

		String name1="Store Product";
		String name2="Search Order";
	
	List<Product> listOfProduct = productService.findAllProducts();
	List<Object[]> orderdetails = productService.orderDetails();
	
	model.addAttribute("products", listOfProduct);
	model.addAttribute("buttonValue1", name1);
	model.addAttribute("buttonValue2", name2);
	model.addAttribute("product", product);
	model.addAttribute("orderdetails", orderdetails);
	
	System.out.println(listOfProduct);
	return "admin";
	}

	//@RequestMapping(value = "/",method = RequestMethod.GET)
	@GetMapping(value = "user")
	public String open1(Model model, Product product) {

		String name1="Store Product";
		String name2="Search Order";

		List<Product> listOfProduct = productService.findAllProducts();
		List<Object[]> orderdetails = productService.orderDetails();

		//List<Object[]> orderdetails2 = productService.listOrders(product.getCategory());

		model.addAttribute("products", listOfProduct);
		model.addAttribute("buttonValue1", name1);
		model.addAttribute("buttonValue2", name2);
		model.addAttribute("product", product);
		model.addAttribute("orderdetails", orderdetails);
		//model.addAttribute("orderdetails2", orderdetails2);

		System.out.println(listOfProduct);
		return "user";
	}

	//@RequestMapping(value = "/",method = RequestMethod.GET)
/*	@GetMapping(value = "/listOrders")
	public String open3(Model model, Product product) {

		String name1="Store Product";
		String name2="Order";

		List<Product> listOfProduct = productService.findAllProducts();
		List<Object[]> orderdetails = productService.listOrders(product.getCategory());

		model.addAttribute("products", listOfProduct);
		model.addAttribute("orderdetails", orderdetails);
		model.addAttribute("buttonValue1", name1);
		model.addAttribute("buttonValue2", name2);

		System.out.println(orderdetails);
		return "admin";
	}*/

	@GetMapping(value = "/listOrders")
	public String open3(Model model, @RequestParam String category,
		@RequestParam String startDate,
		@RequestParam String endDate) {

		String name1="Store Product";
		String name2="Search Order";

		//DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
		LocalDateTime startDateTime = LocalDateTime.parse(startDate, formatter);
		LocalDateTime endDateTime = LocalDateTime.parse(endDate, formatter);

		List<Product> listOfProduct = productService.findAllProducts();
		List<Object[]> orderdetails = productService.listOrders(category, startDateTime, endDateTime);

		model.addAttribute("product", new Product());
		model.addAttribute("products", listOfProduct);
		model.addAttribute("buttonValue1", name1);
		model.addAttribute("buttonValue2", name2);
		model.addAttribute("orderdetails", orderdetails);

		System.out.println(orderdetails);
		return "admin"; // Ensure "admin" is the correct Thymeleaf template name
	}
	
	//@RequestMapping(value = "/addProduct",method = RequestMethod.POST)
	@PostMapping(value = "/addProduct")
	public String addProductDetails(Model model, Product product,HttpServletRequest req) {
		String b1 = req.getParameter("b1");
		String result="";
		//String name1="";
		if(b1.equals("Store Product")) {
			result = productService.storeProduct(product);
		}else {
			result = productService.updateProduct(product);
		}
		String name1="Store Product";
		String name2="Search Order";
		product.setPid(0);
		product.setPname("");
		product.setPrice(0);
		product.setCategory("");
		model.addAttribute("product", product);
		List<Product> listOfProduct = productService.findAllProducts();
		List<Object[]> orderdetails = productService.orderDetails();
		model.addAttribute("orderdetails", orderdetails);
		model.addAttribute("products", listOfProduct);
		model.addAttribute("msg", result);
		model.addAttribute("buttonValue1", name1);
		model.addAttribute("buttonValue2", name2);
		return "admin";
	}
	
	@RequestMapping(value = "/deleteProduct",method = RequestMethod.GET)
	public String deleteProductById(Model model, Product product,HttpServletRequest req) {
		int pid = Integer.parseInt(req.getParameter("pid"));
		System.out.println("pid is "+pid);
		String name = "Store Product";
		String result = productService.deleteProduct(pid);
		List<Product> listOfProduct = productService.findAllProducts();
		model.addAttribute("products", listOfProduct);
		model.addAttribute("product", product);
		model.addAttribute("msg", result);
		model.addAttribute("buttonValue", name);
		List<Object[]> orderdetails = productService.orderDetails();
		model.addAttribute("orderdetails", orderdetails);
	return "admin";
	}
	
	@RequestMapping(value = "/updateProduct",method = RequestMethod.GET)
	public String searchProductById(Model model, HttpServletRequest req) {
		int pid = Integer.parseInt(req.getParameter("pid"));
		String name="Update Product"; 
		Product product = productService.searchProductById(pid);
		List<Product> listOfProduct = productService.findAllProducts();
		model.addAttribute("products", listOfProduct);
		model.addAttribute("product", product);
		model.addAttribute("buttonValue", name);
		List<Object[]> orderdetails = productService.orderDetails();
		model.addAttribute("orderdetails", orderdetails);
		//model.addAttribute("msg", result);
		
	return "admin";
	}

	@RequestMapping(value = "/orderPlace",method = RequestMethod.GET)
	public String placeOrder(Model model, HttpServletRequest req, Orders order,Product product) {
		int pid = Integer.parseInt(req.getParameter("pid"));
		order.setPid(pid);
		String name="Store Product"; 
		String result = ordersService.placeOrder(order);
		List<Product> listOfProduct = productService.findAllProducts();
		model.addAttribute("products", listOfProduct);
		model.addAttribute("product", product);
		model.addAttribute("msg", result);
		model.addAttribute("buttonValue", name);
		//model.addAttribute("msg", result);
		List<Object[]> orderdetails = productService.orderDetails();
		model.addAttribute("orderdetails", orderdetails);
	return "user";
	}

}
