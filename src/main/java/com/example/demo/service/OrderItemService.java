package com.example.demo.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Order_item;
import com.example.demo.model.Orders;
import com.example.demo.model.Products;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.OrdersRepository;
import com.example.demo.repository.ProductRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderItemService {
	@Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrdersRepository ordersRepository; 
    
    @Autowired
    private ProductRepository productRepository;
    
    @Transactional
    public Order_item createOrder_items(
            Integer order_id,
            Integer product_id,
            Integer adult_double_qty,
            Integer adult_single_qty,
            Integer infant_qty) {

        Orders orders = ordersRepository.findById(order_id)
                .orElseThrow(() ->
                    new RuntimeException("找不到對應的訂單 ID: " + order_id));

        Products products = productRepository.findById(product_id)
                .orElseThrow(() ->
                    new RuntimeException("找不到對應的商品 ID: " + product_id));

        // ==========================================
        // 1. 計算本次訂單需要幾個機位
        // ==========================================

        int reservedQuantity =
                adult_double_qty
                + adult_single_qty
                + infant_qty;


        // ==========================================
        // 2. 檢查剩餘機位是否足夠
        // ==========================================

        if (products.getAvailable_capacity() < reservedQuantity) {

            throw new RuntimeException(
                    "剩餘機位不足，目前只剩 "
                    + products.getAvailable_capacity()
                    + " 個機位"
            );
        }


        // ==========================================
        // 3. 建立 Order_item
        // ==========================================

        Order_item order_item = new Order_item();

        order_item.setOrders(orders);
        order_item.setProducts(products);

        order_item.setAdult_double_qty(adult_double_qty);
        order_item.setAdult_single_qty(adult_single_qty);
        order_item.setInfant_qty(infant_qty);


        // ==========================================
        // 4. 取得下單當下的商品價格
        // ==========================================

        BigDecimal adultDoublePrice =
                products.getAdult_double_price();

        BigDecimal adultSinglePrice =
                products.getAdult_single_price();

        BigDecimal infantPrice =
                products.getInfant_price();


        order_item.setAdult_double_unit_price(
                adultDoublePrice
        );

        order_item.setAdult_single_unit_price(
                adultSinglePrice
        );

        order_item.setInfant_unit_price(
                infantPrice
        );


        // ==========================================
        // 5. 計算訂單明細小計
        // ==========================================

        BigDecimal subtotal =
                adultDoublePrice
                    .multiply(
                        BigDecimal.valueOf(adult_double_qty)
                    )
                    .add(
                        adultSinglePrice
                            .multiply(
                                BigDecimal.valueOf(adult_single_qty)
                            )
                    )
                    .add(
                        infantPrice
                            .multiply(
                                BigDecimal.valueOf(infant_qty)
                            )
                    );

        order_item.setSubtotal(subtotal);


        // ==========================================
        // 6. 更新 Orders
        // ==========================================

        orders.setTotal_amount(subtotal);

        orders.setReserved_quantity(reservedQuantity);

        ordersRepository.save(orders);


        // ==========================================
        // 7. ★★★ 真正占用商品機位 ★★★
        // ==========================================

        products.setAvailable_capacity(
                products.getAvailable_capacity()
                        - reservedQuantity
        );
        if (products.getAvailable_capacity() == 0) {
        	products.setProduct_status("TOUR_FULL");
        }
        

        productRepository.save(products);


        // ==========================================
        // 8. 儲存 Order_item
        // ==========================================

        return orderItemRepository.save(order_item);
    }
//    public Order_item createOrder_items(Integer order_id, Integer product_id,
//    			Integer adult_double_qty,Integer adult_single_qty, Integer infant_qty,
//    			BigDecimal adult_double_unit_price, BigDecimal adult_single_unit_price,
//    			BigDecimal infant_unit_price) {
//        Orders orders = ordersRepository.findById(order_id)
//                .orElseThrow(() -> new RuntimeException("找不到對應的訂單 ID: " + order_id));
//        Products products = productRepository.findById(product_id)
//                .orElseThrow(() -> new RuntimeException("找不到對應的商品 ID: " + product_id));
//        Order_item order_item = new Order_item();
//        order_item.setOrders(orders); 
//        order_item.setProducts(products);
//        order_item.setAdult_double_qty(adult_double_qty);
//        order_item.setAdult_single_qty(adult_single_qty);
//        order_item.setInfant_qty(infant_qty);
//        order_item.setAdult_double_unit_price(adult_double_unit_price);
//        order_item.setAdult_single_unit_price(adult_single_unit_price);
//        order_item.setInfant_unit_price(infant_unit_price);
//        //order_item.setSubtotal(subtotal);
//        return orderItemRepository.save(order_item);
//    }

    public Optional<Order_item> getOrder_itemById(Integer order_item_id) {
        return orderItemRepository.findById(order_item_id);
    }

    public Optional<Order_item> getOrder_itemByOrderId(Integer order_id) {
        return orderItemRepository.findByOrdersOrderId(order_id);
    }
    public List<Order_item> getAllOrder_items() {
        return orderItemRepository.findAll();
    }

    @Transactional
    public Order_item updateOrder_items(
            Integer order_item_id,
            Integer order_id,
            Integer product_id,
            Integer adult_double_qty,
            Integer adult_single_qty,
            Integer infant_qty) {

        Order_item order_items = orderItemRepository.findById(order_item_id)
                .orElseThrow(() ->
                    new RuntimeException(
                        "訂單明細不存在: " + order_item_id
                    )
                );

        Orders orders = ordersRepository.findById(order_id)
                .orElseThrow(() ->
                    new RuntimeException(
                        "找不到對應的訂單 ID: " + order_id
                    )
                );

        Products products = productRepository.findById(product_id)
                .orElseThrow(() ->
                    new RuntimeException(
                        "找不到對應的商品 ID: " + product_id
                    )
                );

        // ==========================================
        // 1. 先記錄修改前的人數
        // ==========================================

        int oldReservedQuantity =
                order_items.getAdult_double_qty()
                + order_items.getAdult_single_qty()
                + order_items.getInfant_qty();

        // ==========================================
        // 2. 計算修改後的人數
        // ==========================================

        int newReservedQuantity =
                adult_double_qty
                + adult_single_qty
                + infant_qty;

        // ==========================================
        // 3. 計算機位差額
        // ==========================================

        int difference =
                newReservedQuantity - oldReservedQuantity;

        // ==========================================
        // 4. 檢查增加的人數是否超過剩餘機位
        // ==========================================

        if (difference > 0 &&
            products.getAvailable_capacity() < difference) {

            throw new RuntimeException(
                "剩餘機位不足，目前只剩 "
                + products.getAvailable_capacity()
                + " 個機位"
            );
        }

        // ==========================================
        // 5. 更新 Order_item 數量
        // ==========================================

        order_items.setOrders(orders);
        order_items.setProducts(products);

        order_items.setAdult_double_qty(adult_double_qty);
        order_items.setAdult_single_qty(adult_single_qty);
        order_items.setInfant_qty(infant_qty);

        // ==========================================
        // 6. 重新計算 subtotal
        // ==========================================

        BigDecimal subtotal =
                order_items.getAdult_double_unit_price()
                    .multiply(
                        BigDecimal.valueOf(adult_double_qty)
                    )
                    .add(
                        order_items.getAdult_single_unit_price()
                            .multiply(
                                BigDecimal.valueOf(adult_single_qty)
                            )
                    )
                    .add(
                        order_items.getInfant_unit_price()
                            .multiply(
                                BigDecimal.valueOf(infant_qty)
                            )
                    );

        order_items.setSubtotal(subtotal);

        // ==========================================
        // 7. 更新 Orders
        // ==========================================

        orders.setTotal_amount(subtotal);
        orders.setReserved_quantity(newReservedQuantity);

        // ==========================================
        // 8. 更新 Products 機位
        // ==========================================

        products.setAvailable_capacity(
            products.getAvailable_capacity() - difference
        );

        // ==========================================
        // 9. 儲存
        // ==========================================

        productRepository.save(products);
        ordersRepository.save(orders);

        return orderItemRepository.save(order_items);
    }

    public boolean deleteOrder_items(Integer order_item_id) {
        Optional<Order_item> order_item = orderItemRepository.findById(order_item_id);
        if (order_item.isPresent()) {
        	orderItemRepository.deleteById(order_item_id);
            return true;
        }
        return false;
    }

    public long getOrder_itemsCount() {
        return orderItemRepository.count();
    }
}
