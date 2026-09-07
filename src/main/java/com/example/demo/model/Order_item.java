package com.example.demo.model;

import java.math.BigDecimal;

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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Order_item")       
public class Order_item {
	@Id
	@Column(name = "order_item_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)  
    private Integer order_item_id;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)      
    @JsonIgnoreProperties("order_item")
	private Orders orders;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)       
    @JsonIgnoreProperties("order_item")
	private Products products;

	@Column(name = "adult_double_qty", nullable = false)   
    private Integer adult_double_qty;
	@Column(name = "adult_single_qty", nullable = false)   
    private Integer adult_single_qty;
	@Column(name = "infant_qty", nullable = false)   
    private Integer infant_qty;
	@Column(name = "adult_double_unit_price", precision = 10, scale = 2, nullable = false)  
    private BigDecimal adult_double_unit_price;
	@Column(name = "adult_single_unit_price", precision = 10, scale = 2, nullable = false)  
    private BigDecimal adult_single_unit_price;
	@Column(name = "infant_unit_price", precision = 10, scale = 2, nullable = false) 
    private BigDecimal infant_unit_price;
	@Column(name = "subtotal", precision = 10, scale = 2) 
    private BigDecimal subtotal;
	
	@PrePersist
	@PreUpdate
	public void calculateSubtotal() {
		BigDecimal total = BigDecimal.ZERO;
		if (adult_double_qty != null && adult_double_unit_price != null) {
			total = total.add(BigDecimal.valueOf(adult_double_qty).multiply(adult_double_unit_price));
		}		
		if (adult_single_qty != null && adult_single_unit_price != null) {
			total = total.add(BigDecimal.valueOf(adult_single_qty).multiply(adult_single_unit_price));
		}
		if (infant_qty != null && infant_unit_price != null) {
			total = total.add(BigDecimal.valueOf(infant_qty).multiply(infant_unit_price));
		}

		this.subtotal = total;
	}
	@Override
    public String toString() {
        return "Order_item {order_item_id='" + order_item_id
                + "', order_id='" + (orders != null ? orders.getOrder_id() : null)
                + "', product_id='" + (products != null ? products.getProduct_id() : null)
                + "', adult_double_qty='" + adult_double_qty
                + "', adult_single_qty=" + adult_single_qty
                + "', infant_qty='" + infant_qty
                + "', adult_double_unit_price='" + adult_double_unit_price
                + "', adult_single_unit_price='" + adult_single_unit_price
                + "', infant_unit_price='" + infant_unit_price
                + "', subtotal='" + subtotal
                + "}";
    }
}
