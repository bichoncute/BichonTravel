package com.example.demo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Products;
@Repository
public interface ProductRepository extends JpaRepository<Products, Integer> {
	  // 查詢出發日期介於開始日期與結束日期之間的商品
	//SELECT * FROM products WHERE fly_day BETWEEN startDate AND endDate;
//	 List<Products> findByFly_dayBetween(
//		        LocalDate startDate,
//		        LocalDate endDate
//		    );
	 
	 @Query("""
		        SELECT p
		        FROM Products p
		        WHERE p.fly_day BETWEEN :startDate AND :endDate
		    """)
		    List<Products> findProductsByFlyDayBetween(
		        @Param("startDate") LocalDate startDate,
		        @Param("endDate") LocalDate endDate
		    );
}

