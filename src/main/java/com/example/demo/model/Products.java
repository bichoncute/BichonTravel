
package com.example.demo.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


import com.fasterxml.jackson.annotation.JsonFormat;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Products")       // 對應資料庫中的 products 表
public class Products {
    @Id
    @Column(name = "product_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // MySQL AUTO_INCREMENT
    private Integer product_id;

    @Column(nullable = false, length = 50)   // NOT NULL：商品名稱必填
    private String name;
    @Column(nullable = false, precision = 10, scale = 2)   // NOT NULL：價格必填
    private BigDecimal adult_double_price;
    @Column(nullable = false, precision = 10, scale = 2)   // NOT NULL：價格必填
    private BigDecimal adult_single_price;
    @Column(nullable = false, precision = 10, scale = 2)   // NOT NULL：價格必填
    private BigDecimal infant_price;   
    @Column(length = 50)
    private String departure_airport;
    @Column(nullable = false, length = 50)
    private String destination;
    @Column(nullable = false)   // NOT NULL：可售人數必填
    private Integer max_capacity;
    @Column(nullable = false)   // NOT NULL：剩餘人數必填
    private Integer available_capacity;
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    @Column(name = "notification", columnDefinition = "TEXT")
    private String notification;
    @Column(nullable = false, length = 20)   // NOT NULL：狀態必填
    private String product_status;      // 允許 null：庫存可以不設定
    @Column(nullable = false)   // NOT NULL：出發日必填
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fly_day;
    @Column(nullable = false)   // NOT NULL：回程日必填
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate back_day;
    @Column(name = "created_at", updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created_at;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updated_at;
    @Version
    private Integer product_version;   
    
    @OneToMany(mappedBy = "products",cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
    	private List<Image> images = new ArrayList<>();
    @OneToMany(mappedBy = "products",cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
    private List<Cart_items> cart_items = new ArrayList<>();
    @OneToMany(mappedBy = "products",cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
    private List<Browse_history> browse_history = new ArrayList<>();
    @OneToMany(mappedBy = "products",cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
    private List<Order_item> order_item = new ArrayList<>();
    // 新增資料之前執行
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.created_at = now;
        this.updated_at = now;
    }
    // 更新資料之前執行
    @PreUpdate
    protected void onUpdate() {
        this.updated_at = LocalDateTime.now();
    }
    public Products() {}
	public Products(String name, BigDecimal adult_double_price, 
			BigDecimal adult_single_price,BigDecimal infant_price,
			String departure_airport, String destination, Integer max_capacity, 
			Integer available_capacity,String description, String notification, 
			String product_status, LocalDate fly_day, LocalDate back_day) {
		this.name = name;
		this.adult_double_price = adult_double_price;
		this.adult_single_price = adult_single_price;
		this.infant_price = infant_price;
		this.departure_airport = departure_airport;
		this.destination = destination;
		this.max_capacity = max_capacity;
		this.available_capacity = available_capacity;
		this.description = description;
		this.notification = notification;
		this.product_status = product_status;
		this.fly_day = fly_day;
		this.back_day = back_day;	
		
	}
    // 帶參數建構子，方便在測試或 Service 中快速建立物件
    @Override
    public String toString() {
        return "Products{product_id='" + product_id
                + "', name='" + name
                + "', adult_double_price='" + adult_double_price
                + "', adult_single_price='" + adult_single_price
                + "', infant_price='" + infant_price
                + "', departure_airport='" + departure_airport
                + "', destination='" + destination
                + "', max_capacity='" + max_capacity
                + "', available_capacity='" + available_capacity
                + "', description='" + description
                + "', notification='" + notification
                + "', product_status='" + product_status
                + "', fly_day='" + fly_day
                + "', back_day='" + back_day
                + "', product_version='" + product_version
                + "}";
    }
}