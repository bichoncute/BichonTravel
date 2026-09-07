package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Cart_items;
import com.example.demo.model.Orders;
import java.util.Optional;
@Repository
public interface CartItemsRepository extends JpaRepository<Cart_items, Integer>   {
	@Query("""
            SELECT ci
            FROM Cart_items ci
            WHERE ci.users.id = :user_id
            """)
    List<Cart_items> findByUser_id(
            @Param("user_id") Integer user_id
    );

    @Query("""
            SELECT COUNT(ci)
            FROM Cart_items ci
            WHERE ci.users.id = :user_id
            """)
    Integer getCartItemsCountByUser_id(
            @Param("user_id") Integer user_id
    );
    @Query("""
	        SELECT b
	        FROM Cart_items b
	        WHERE b.users.id = :user_id
	          AND b.products.product_id = :product_id
	    """)
    Optional<Cart_items> findByUsers_IdAndProducts_Product_id(
            Integer user_id,
            Integer product_id
    );
}
