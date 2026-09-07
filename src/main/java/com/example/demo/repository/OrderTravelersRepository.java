package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Order_travelers;
@Repository
public interface OrderTravelersRepository extends JpaRepository<Order_travelers, Integer> {
	@Query("""
	        SELECT ot
	        FROM Order_travelers ot
	        WHERE ot.orders.order_id = :order_id
	    """)
	    List<Order_travelers> findByOrderId(@Param("order_id") Integer order_id);
}
