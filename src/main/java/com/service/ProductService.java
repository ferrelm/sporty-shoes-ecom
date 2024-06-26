package com.service;

import com.entity.Product;
import com.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

	@Autowired
	ProductRepository productRepository;
	
	public List<Product> findAllProducts() {
		return productRepository.findAll();		// select * from product in SQL
	}											// select p from Product p in HQL/JPQL
	
	
	public String storeProduct(Product product) {
		Optional<Product> result=productRepository.findById(product.getPid());
		if(result.isPresent()) {
			return "Product id must be unique";
		}else {
			//product.setQuantity(product.getQuantity());
			productRepository.save(product);
			return "Product record stored successfully";
		}
	
	}
	
	public String deleteProduct(int pid) {
		System.out.println("product id is "+pid);
		Optional<Product> result=productRepository.findById(pid);
		if(result.isPresent()) {
			Product p = result.get();
			productRepository.delete(p);
			return "Product deleted successfully";
		}else {
			return "Product record not present";
		}
	}
	
	public Product searchProductById(int pid) {
		Optional<Product> result=productRepository.findById(pid);
		if(result.isPresent()) {
			Product product = result.get();
			return product;
		}else {
			return null;
		}
	}
	
		public String updateProduct(Product product) {
			Optional<Product> result=productRepository.findById(product.getPid());
			if(result.isPresent()) {
				Product existingProduct = result.get();
				existingProduct.setPname(product.getPname());
				existingProduct.setPrice(product.getPrice());
				existingProduct.setCategory(product.getCategory());
				existingProduct.setQuantity(product.getQuantity());
				productRepository.saveAndFlush(existingProduct);
				return "Product updated successfully";
			}else {
				return "Product record not present";
			}
		}
		
		public List<Object[]> orderDetails() {
			return productRepository.orderDetails();		// custom methods 
		}

	public List<Object[]> orderDetailsByUser(String username) {
		return productRepository.orderDetailsByUser(username);		// custom methods
	}

	public List<Object[]> listOrders(String category, LocalDateTime startDateTime, LocalDateTime endDateTime) {
		return productRepository.listOrders(category, startDateTime, endDateTime);		// custom methods
	}
		
}





