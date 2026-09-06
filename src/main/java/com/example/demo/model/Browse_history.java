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
	    name = "Browse_history",
	    uniqueConstraints = {
	        @UniqueConstraint(
	            name = "uk_browse_history_user_product",
	            columnNames = {"user_id", "product_id"}
	        )
	    }
	)
public class Browse_history {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "bh_id", nullable = false)
	private Integer bh_id;
	
	@ManyToOne(fetch = FetchType.LAZY)  
    @JoinColumn(name = "user_id", nullable = false) 
    @JsonIgnoreProperties("browse_history")
	private Users users;
	
	@ManyToOne(fetch = FetchType.LAZY)  
    @JoinColumn(name = "product_id", nullable = false)        // 資料庫中的外鍵欄位名稱
    @JsonIgnoreProperties("browse_history")
	private Products products;

	@Column(name = "viewed_at", nullable = false)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime viewed_at;
	
	@PrePersist
    @PreUpdate
    protected void onUpdate() {
        this.viewed_at = LocalDateTime.now();
    }
    @Override
    public String toString() {
        return "Browse_history {bh_id='" + bh_id
                + "', user_id='" + (users != null ? users.getId() : null)
                + "', product_id='" + (products != null ? products.getProduct_id() : null)
                + "', viewed_at='" + viewed_at
                + "}";
    }
}
