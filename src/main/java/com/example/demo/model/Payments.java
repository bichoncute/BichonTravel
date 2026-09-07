package com.example.demo.model;

import java.math.BigDecimal;
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
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Payments")       
public class Payments {
	@Id
	@Column(name = "payment_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Integer payment_id;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)       
    @JsonIgnoreProperties("payments")
	private Orders orders;
	
	@Column(name = "payment_method", length = 30)   
    private String payment_method;
	@Column(name = "payment_status", length = 30)   
    private String payment_status;
	@Column(name = "transaction_id", length = 100)   
    private String transaction_id;
	@Column(name = "amount", nullable = false, precision = 10, scale = 2)  
    private BigDecimal amount;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime start_at;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime paid_at;
	@Column(name = "created_at", updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created_at;
	@Column(name = "updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updated_at;
	@Column(name = "payment_version")
	@Version
	private Integer payment_version;
	
    @Override
    public String toString() {
        return "Payments{payment_id ='" + payment_id 
                + "', order_id='" + (orders != null ? orders.getOrder_id() : null)
                + "', payment_method='" + payment_method
                + "', payment_status='" + payment_status
                + "', transaction_id='" + transaction_id
                + "', amount='" + amount
                + "', start_at='" + start_at
                + "', paid_at='" + paid_at
                + "', created_at='" + created_at
                + "', updated_at='" + updated_at
                + "', payment_version='" + payment_version
                + "}";
    }

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
}
