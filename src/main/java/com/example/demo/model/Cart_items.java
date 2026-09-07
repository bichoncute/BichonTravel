package com.example.demo.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(
	    name = "Cart_items",
	    uniqueConstraints = {
	        @UniqueConstraint(
	            name = "uk_cart_user_product",
	            columnNames = {"user_id", "product_id"}
	        )
	    }
	)
public class Cart_items {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cart_item_id", nullable = false)
	private Integer cart_item_id;
	
	@ManyToOne(fetch = FetchType.LAZY)  
    @JoinColumn(name = "user_id", nullable = false)        
    @JsonIgnoreProperties("cart_items")
	private Users users;
	
	@ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "product_id", nullable = false)        
    @JsonIgnoreProperties("cart_items")
	private Products products;
	@Column(name = "created_at", updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created_at;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updated_at;
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.created_at = now;
        this.updated_at = now;
    }
    @PreUpdate
    protected void onUpdate() {
        this.updated_at = LocalDateTime.now();
    }
    @Override
    public String toString() {
        return "Cart_items {cart_item_id='" + cart_item_id
                + "', user_id='" + (users != null ? users.getId() : null)
                + "', product_id='" + (products != null ? products.getProduct_id() : null)
                + "', created_at='" + created_at
                + "', updated_at='" + updated_at
                + "}";
    }
}
