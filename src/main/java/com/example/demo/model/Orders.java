package com.example.demo.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;
@Data
@Entity
@Table(name = "Orders")     
public class Orders {
	@Id
	@Column(name = "order_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)  
	private Integer order_id;
	
	@ManyToOne(fetch = FetchType.LAZY)  
    @JoinColumn(name = "user_id", nullable = false)      
    @JsonIgnoreProperties("orders")
	private Users users;
	
	@Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
	private BigDecimal total_amount; 
	@Column(name = "order_status", nullable = false)
	private String order_status;
	@Column(name = "reserved_quantity")
	private Integer reserved_quantity;
	@Column(name = "payment_expires_at")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime payment_expires_at;
    @Column(name = "created_at", updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created_at;
    @Column(name = "updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updated_at;
    
    @OneToMany(
    	    mappedBy = "orders",
    	    cascade = CascadeType.PERSIST,
    	    fetch = FetchType.LAZY
    	)
    	private List<Order_travelers> order_travelers = new ArrayList<>();
    	
    	@OneToMany(
        	    mappedBy = "orders",
        	    cascade = CascadeType.PERSIST,
        	    fetch = FetchType.LAZY
        	)
    private List<Payments> payments = new ArrayList<>();
    	
    	@OneToMany(
        	    mappedBy = "orders",
        	    cascade = CascadeType.PERSIST,
        	    fetch = FetchType.LAZY
        	)
    private List<Order_item> order_item = new ArrayList<>();
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
        return "Orders{order_id='" + order_id
                + "', user_id='" + (users != null ? users.getId() : null)
                + "', total_amount='" + total_amount
                + "', order_status='" + order_status
                + "', reserved_quantity='" + reserved_quantity
                + "', payment_expires_at='" + payment_expires_at
                + "', created_at='" + created_at
                + "', updated_at='" + updated_at
                + "}";
    }
}
