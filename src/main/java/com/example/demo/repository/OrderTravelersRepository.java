package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Order_travelers;
@Repository
public interface OrderTravelersRepository extends JpaRepository<Order_travelers, Integer> {
	 // JpaRepository<Product, Long> 的兩個泛型（Generic）：
    //   第一個 Product → 要操作的 Entity 型別
    //   第二個 Long    → Product.id 的型別
    //
    // 繼承後自動擁有：
    //   save()、findById()、findAll()、deleteById()、existsById()、count() 等
    //
    // Day 2 會在這裡新增自訂查詢方法
	//List<Order_travelers> findByNameContaining(String keyword);
	//List<User> findById(String id);
	@Query("""
	        SELECT ot
	        FROM Order_travelers ot
	        WHERE ot.orders.order_id = :order_id
	    """)
	    List<Order_travelers> findByOrderId(@Param("order_id") Integer order_id);
}
