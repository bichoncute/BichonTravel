package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Image;
@Repository
public interface ImageRepository extends JpaRepository<Image, Integer>   {
	 // JpaRepository<Product, Long> 的兩個泛型（Generic）：
    //   第一個 Product → 要操作的 Entity 型別
    //   第二個 Long    → Product.id 的型別
    //
    // 繼承後自動擁有：
    //   save()、findById()、findAll()、deleteById()、existsById()、count() 等
    //
    // Day 2 會在這裡新增自訂查詢方法
	//List<Image> findByNameContaining(String keyword);
	//List<User> findById(String id);
}
