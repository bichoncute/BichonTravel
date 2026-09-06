package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Order_travelers")       
public class Order_travelers {
	@Id
	@Column(name = "travelers_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // MySQL AUTO_INCREMENT
    private Integer travelers_id;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)        // 資料庫中的外鍵欄位名稱
    @JsonIgnoreProperties("order_travelers")
	private Orders orders;

	@Column(name = "travelers_real_name", nullable = false , length = 30)   
    private String travelers_real_name;
	@Column(name = "passport_num", length = 30)   
    private String passport_num;
	@Column(name = "phone_num", length = 20)   
    private String phone_num;
	@Column(name = "line_account", length = 30)   
    private String line_account;
	@Column(name = "special_request", length = 200)   
    private String special_request;
	@Column(name = "traveler_type", nullable = false, length = 20)   
    private String traveler_type;
	@Column(name = "room_type", nullable = false, length = 20)   
    private String room_type;
	@Column(name = "traveler_status", length = 20)   
    private String traveler_status;
	
	 @Override
	    public String toString() {
	        return "Order_travelers{travelers_id='" + travelers_id
	                + "', order_id='" +  (orders != null ? orders.getOrder_id() : null)
	                + "', travelers_real_name='" + travelers_real_name
	                + "', passport_num='" + passport_num
	                + "', phone_num='" + phone_num
	                + "', line_account='" + line_account
	                + "', special_request='" + special_request
	                + "', traveler_type='" + traveler_type
	                + "', room_type='" + room_type
	                + "', traveler_status='" + traveler_status
	                + "}";
	    }
}
