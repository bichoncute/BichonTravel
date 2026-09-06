package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Order_item;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
@Repository
public interface OrderItemRepository
        extends JpaRepository<Order_item, Integer> {

	@Query("SELECT oi FROM Order_item oi WHERE oi.orders.order_id = :order_id")
	Optional<Order_item> findByOrdersOrderId(@Param("order_id") Integer order_id);

}
