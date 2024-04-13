package com.repository;

import com.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>{
	
	@Query("select p from Product p where p.pname = :pname")
	public 	List<Product> findProductByName(@Param("pname") String pname);
	
	
	@Query("select p from Product p where p.price >= :price")
	public 	List<Product> findProductByPrice(@Param("price") float price);

	@Query("select o.oid, l.username, p.pname, p.price, p.category, o.ldt from Product p, Orders o join o.login l where p.pid = o.pid")
	public List<Object[]> orderDetails();

	@Query("select o.oid, l.username, p.pname, p.price, p.category, o.ldt from Product p, Orders o join o.login l where p.pid = o.pid and l.username = :username order by o.oid asc")
	public List<Object[]> orderDetailsByUser(@Param("username") String username);

	@Query("select o.oid, l.username, p.pname, p.price, p.category, o.ldt from Product p, Orders o join o.login l where p.pid = o.pid and p.category = :category and o.ldt "
		+ "between :startDateTime and :endDateTime")
	public List<Object[]> listOrders(
		@Param("category") String category,
		@Param("startDateTime") LocalDateTime startDateTime,
		@Param("endDateTime") LocalDateTime endDateTime);
}
