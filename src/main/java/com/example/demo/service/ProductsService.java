package com.example.demo.service;
import com.example.demo.model.Products;
import com.example.demo.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ProductsService {
	@Autowired
    private ProductRepository pr;
    
    public Products createProduct(String name, BigDecimal adult_double_price, 
    			BigDecimal adult_single_price, BigDecimal infant_price,
			String departure_airport, String destination, Integer max_capacity, 
			Integer available_capacity,String description, String notification, 
			String product_status, LocalDate fly_day, LocalDate back_day
			) {
    	Products products = new Products(name, adult_double_price, adult_single_price, infant_price,
    			departure_airport, destination, max_capacity, available_capacity,
    			description, notification, product_status, fly_day, back_day
    			);
		return pr.save(products);
	}
    public Optional<Products> getProductsById(Integer product_id) {
        return pr.findById(product_id);
    }
    
    public List<Products> getAllProducts() {
        return pr.findAll();
    }
    public List<Products> searchByFly_day(
            LocalDate startDate,
            LocalDate endDate) {

        return pr.findProductsByFlyDayBetween(
            startDate,
            endDate
        );
    }
    public Products updateProducts(Integer product_id,String name, BigDecimal adult_double_price, 
    			BigDecimal infant_price, BigDecimal adult_single_price, 
			String departure_airport, String destination, Integer max_capacity, 
			Integer available_capacity,String description, String notification, 
			String product_status, LocalDate fly_day, LocalDate back_day
			) {
        Optional<Products> existingUser = pr.findById(product_id);
        if (existingUser.isPresent()) {
        	Products product = existingUser.get();
        	product.setName(name);
        	product.setAdult_double_price(adult_double_price);
        	product.setAdult_single_price(adult_single_price);
        	product.setInfant_price(infant_price);
        	product.setDeparture_airport(departure_airport);
        	product.setDestination(destination);
        	product.setMax_capacity(max_capacity);
        	product.setAvailable_capacity(available_capacity);
        	product.setDescription(description);
        	product.setNotification(notification);
        	product.setProduct_status(product_status);
        	product.setFly_day(fly_day);
        	product.setBack_day(back_day);
            return pr.save(product);
        }

        throw new RuntimeException("商品不存在: " + product_id);
    }
      
    public boolean deleteProducts(Integer product_id) {
        if (pr.existsById(product_id)) {
            pr.deleteById(product_id);
            return true;
        }
        return false;
    }
    public long getProductsCount() {
        return pr.count();
    }
}
