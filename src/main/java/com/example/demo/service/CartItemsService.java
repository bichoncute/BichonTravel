package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Cart_items;
import com.example.demo.model.Products;
import com.example.demo.model.Users;
import com.example.demo.repository.CartItemsRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;

@Service
public class CartItemsService {
	@Autowired
    private CartItemsRepository cartItemsRepository;

    @Autowired
    private UserRepository userRepository; 
    
    @Autowired
    private ProductRepository productRepository;
    
    public Cart_items createCart_items(
            Integer user_id,
            Integer product_id) {

        Users users = userRepository.findById(user_id)
                .orElseThrow(() ->
                    new RuntimeException("找不到對應的使用者 ID: " + user_id));

        Products products = productRepository.findById(product_id)
                .orElseThrow(() ->
                    new RuntimeException("找不到對應的商品 ID: " + product_id));


        // 第一次瀏覽 → 建立紀錄
        Cart_items Cart_items = new Cart_items();

        Cart_items.setUsers(users);
        Cart_items.setProducts(products);

        return cartItemsRepository.save(Cart_items);
    }

    public Optional<Cart_items> getCart_itemsById(Integer cart_item_id) {
        return cartItemsRepository.findById(cart_item_id);
    }
    
    public List<Cart_items> getAllCart_items() {
        return cartItemsRepository.findAll();
    }

    public List<Cart_items> getByUser_id(Integer user_id){
    	return cartItemsRepository.findByUser_id(user_id);
    }
    public Integer getCartsCountByUser_id(Integer user_id){
    		return cartItemsRepository.getCartItemsCountByUser_id(user_id);
    }
    public Cart_items updateCart_items(Integer cart_item_id, Integer user_id, 
    			Integer product_id) {
        
        Optional<Cart_items> existingCart_items = cartItemsRepository.findById(cart_item_id);

        if (existingCart_items.isPresent()) {
        		Cart_items Cart_items = existingCart_items.get();

            // 根據傳入的 order_id 尋找訂單物件
        		Users users = userRepository.findById(user_id)
                    .orElseThrow(() -> new RuntimeException("找不到對應的使用者 ID: " + user_id));
            Products products = productRepository.findById(product_id)
                    .orElseThrow(() -> new RuntimeException("找不到對應的商品 ID: " + product_id));
            Cart_items.setUsers(users); 
            Cart_items.setProducts(products);
           
            return cartItemsRepository.save(Cart_items);
        }

        throw new RuntimeException("不存在: " + cart_item_id); 
    }
    
     public boolean deleteByUserAndProduct(
            Integer user_id,
            Integer product_id) {

        Optional<Cart_items> cartItem =
                cartItemsRepository.findByUsers_IdAndProducts_Product_id(
                        user_id,
                        product_id
                );

        if (cartItem.isPresent()) {

            cartItemsRepository.delete(cartItem.get());

            return true;
        }

        return false;
    }
    public boolean deleteCart_items(Integer cart_item_id) {
        Optional<Cart_items> Cart_items = cartItemsRepository.findById(cart_item_id);
        if (Cart_items.isPresent()) {
        		cartItemsRepository.deleteById(cart_item_id);
            return true;
        }
        return false;
    }

    public long getCart_itemsCount() {
        return cartItemsRepository.count();
    }
}
