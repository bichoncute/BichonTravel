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
	 // JpaRepository<Product, Long> 的兩個泛型（Generic）：
    //   第一個 Product → 要操作的 Entity 型別
    //   第二個 Long    → Product.id 的型別
    //
    // 繼承後自動擁有：
    //   save()、findById()、findAll()、deleteById()、existsById()、count() 等
    //
    // Day 2 會在這裡新增自訂查詢方法
	//List<Cart_items> findByNameContaining(String keyword);
	//List<User> findById(String id);
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
    // 找出「某個會員 + 某個商品」的購物車紀錄
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
