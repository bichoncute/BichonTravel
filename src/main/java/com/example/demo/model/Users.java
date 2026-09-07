package com.example.demo.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;
@Data
@Entity
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id; 
    @Column(name = "name", length = 20)
    private String name;
    @Column(name = "email", nullable = false, length = 100, unique = true)
    private String email;
    @Column(name = "password_hash", nullable = false, length = 255)
    private String password_hash;
    @Column(name = "role", nullable = false, length = 20)
    private String role;
    @Column(name = "phone", length = 20)
    private String phone;
    @Column(name = "account_status", length = 20)
    private String account_status = "active";
    @Column(name = "created_at", updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created_at;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updated_at;  
    @OneToMany(mappedBy = "users",cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
    	private List<Orders> orders = new ArrayList<>();
    @OneToMany(mappedBy = "users",cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
	private List<Cart_items> cart_items = new ArrayList<>();
	@OneToMany(mappedBy = "users",cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
	private List<Browse_history> browse_history = new ArrayList<>();
    
    
    public Users() {}
    public Users(String name,String email,String role,String phone,String password_hash
    		,String account_status) {
        this.name = name;
        this.email = email;
        this.role = role;
        this.phone = phone;
        this.password_hash = password_hash;
        this.account_status  = account_status;
    }
    
    @Override
    public String toString() {
        	return "Users{id='" + id
                + "', name='" + name
                + "', role='" + role
                + "', email='" + email
                + "', phone='" + phone
                + "', password_hash='" + password_hash
                + "', account_status='" + account_status
                + "', created_at='" + created_at
                + "', updated_at='" + updated_at
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
