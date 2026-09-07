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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Image") 
public class Image {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "image_id", nullable = false)
	private Integer image_id;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)       
    @JsonIgnoreProperties("images")
	private Products products;
	
	@Column(name = "image_name", length = 255)
	private String image_name;
	@Column(name = "image_url", length = 1000)
	private String image_url;
	@Column(name = "created_at", updatable = false)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created_at;
	@Column(name = "updated_at")
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
        return "Image{image_id='" + image_id
                + "', product_id='" + (products != null ? products.getProduct_id() : null)
                + "', image_name='" + image_name
                + "', image_url='" + image_url
                + "', created_at='" + created_at
                + "', updated_at='" + updated_at
                + "}";
    		}
}
