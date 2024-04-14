package com.service;

import com.entity.Orders;
import com.entity.Product;
import com.repository.OrdersRepository;
import com.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OrdersService {

	@Autowired
	OrdersRepository ordersRepository;

	@Autowired
	ProductRepository productRepository;

	public String placeOrder(Orders orders) {
		Product product = productRepository.findById(orders.getPid()).orElse(null);
		if(product != null && product.getQuantity() > 0) {
			product.setQuantity(product.getQuantity() - 1);  // Decrease quantity by one
			productRepository.save(product);
			orders.setLdt(LocalDateTime.now());
			ordersRepository.save(orders);
			return "Order placed successfully for product " + orders.getPid();
		} else {
			return "Product is out of stock";
		}
	}
}
