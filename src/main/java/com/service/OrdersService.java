package com.service;

import com.entity.Orders;
import com.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OrdersService {

	@Autowired
	OrdersRepository ordersRepository;
	
	public String placeOrder(Orders orders) {
			// oid auto increment we need only pid as fk 
			orders.setLdt(LocalDateTime.now());
		
		ordersRepository.save(orders);
		return "Order placed successfully for product "+orders.getPid();
	}
}
