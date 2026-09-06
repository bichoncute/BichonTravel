package com.example.demo.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.demo.model.Orders;
@Repository
public interface OrdersRepository extends JpaRepository<Orders, Integer> {
	
	@Query("""
            SELECT o
            FROM Orders o
            WHERE o.users.id = :user_id
            """)
    List<Orders> findByUser_id(
            @Param("user_id") Integer user_id
    );

    @Query("""
            SELECT COUNT(o)
            FROM Orders o
            WHERE o.users.id = :user_id
            """)
    Integer getOrdersCountByUser_id(
            @Param("user_id") Integer user_id
    );
}
