package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Browse_history;
import com.example.demo.model.Cart_items;

@Repository
public interface BrowseHistoryRepository extends JpaRepository<Browse_history, Integer>  {
	 // JpaRepository<Product, Long> 的兩個泛型（Generic）：
    //   第一個 Product → 要操作的 Entity 型別
    //   第二個 Long    → Product.id 的型別
    //
    // 繼承後自動擁有：
    //   save()、findById()、findAll()、deleteById()、existsById()、count() 等
    //
    // Day 2 會在這裡新增自訂查詢方法
	//List<Browse_history> findByNameContaining(String keyword);
	//List<User> findById(String id);
	
	
//	Optional<Browse_history> findByUsers_IdAndProducts_Product_id(
//	        Integer user_id,
//	        Integer product_id
//	);
	@Query("""
	        SELECT b
	        FROM Browse_history b
	        WHERE b.users.id = :user_id
	          AND b.products.product_id = :product_id
	    """)
	    Optional<Browse_history> findByUserIdAndProductId(
	            @Param("user_id") Integer user_id,
	            @Param("product_id") Integer product_id
	    );
	    
	    @Query("""
	            SELECT b
	            FROM Browse_history b
	            WHERE b.users.id = :user_id
	            """)
	    List<Browse_history> findByUser_id(
	            @Param("user_id") Integer user_id
	    );

	    @Query("""
	            SELECT COUNT(b)
	            FROM Browse_history b
	            WHERE b.users.id = :user_id
	            """)
	    Integer getBrowse_historyCountByUser_id(
	            @Param("user_id") Integer user_id
	    );
}
