package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Order_travelers;
import com.example.demo.model.Orders;
import com.example.demo.repository.OrderTravelersRepository;
import com.example.demo.repository.OrdersRepository; // 🚀 新增：需要這個來查詢 Orders 物件

@Service
public class TravelerService /*implements CommandLineRunner*/ {

    @Autowired
    private OrderTravelersRepository orderTravelersRepository;

    @Autowired
    private OrdersRepository ordersRepository; // 🚀 新增：注入 Orders 的 Repository

    // 修正 1：將第一個參數改為 Integer order_id，並在內部補上 Orders 查詢
    public Order_travelers createOrder_travelers(Integer order_id, String travelers_real_name,
            String passport_num, String phone_num, String line_account,
            String special_request, String traveler_type, String room_type,
            String traveler_status) {

        // 根據前端傳來的 order_id 查出 Orders 物件
        Orders orders = ordersRepository.findById(order_id)
                .orElseThrow(() -> new RuntimeException("找不到對應的訂單 ID: " + order_id));

        // 建議不使用 new 改用 setter，避免建構子參數順序對不上的問題
        Order_travelers order_travelers = new Order_travelers();
        order_travelers.setOrders(orders); // 🚀 正確塞入 Orders 物件
        order_travelers.setTravelers_real_name(travelers_real_name);
        order_travelers.setPassport_num(passport_num);
        order_travelers.setPhone_num(phone_num);
        order_travelers.setLine_account(line_account);
        order_travelers.setSpecial_request(special_request);
        order_travelers.setTraveler_type(traveler_type);
        order_travelers.setRoom_type(room_type);
        order_travelers.setTraveler_status(traveler_status);

        return orderTravelersRepository.save(order_travelers);
    }

    public Optional<Order_travelers> getOrder_travelersById(Integer id) {
        return orderTravelersRepository.findById(id);
    }

    public List<Order_travelers> getOrderTravelersByOrderId(Integer order_id) {
        return orderTravelersRepository.findByOrderId(order_id);
    }
    public List<Order_travelers> getAllOrder_travelers() {
        return orderTravelersRepository.findAll();
    }

    public Order_travelers updateOrder_travelers(Integer travelers_id, Integer order_id,
            String travelers_real_name, String passport_num, String phone_num, String line_account,
            String special_request, String traveler_type, String room_type,
            String traveler_status) {
        
        // 修正 2：將 id 改為正確的參數名稱 travelers_id
        Optional<Order_travelers> existingOrder_travelers = orderTravelersRepository.findById(travelers_id);

        if (existingOrder_travelers.isPresent()) {
            Order_travelers order_travelers = existingOrder_travelers.get();

            // 根據傳入的 order_id 尋找訂單物件
            Orders orders = ordersRepository.findById(order_id)
                    .orElseThrow(() -> new RuntimeException("找不到對應的訂單 ID: " + order_id));

            order_travelers.setTravelers_id(travelers_id);
            order_travelers.setOrders(orders); // 🚀 修正 3：必須塞入 Orders 物件，而非 Integer
            order_travelers.setTravelers_real_name(travelers_real_name);
            order_travelers.setPassport_num(passport_num);
            order_travelers.setPhone_num(phone_num);
            order_travelers.setLine_account(line_account);
            order_travelers.setSpecial_request(special_request);
            order_travelers.setTraveler_type(traveler_type);
            order_travelers.setRoom_type(room_type);
            order_travelers.setTraveler_status(traveler_status);

            return orderTravelersRepository.save(order_travelers);
        }

        throw new RuntimeException("使用者不存在: " + travelers_id); // 修正 2
    }

    public boolean deleteOrder_travelers(Integer id) {
        // 修正 4：Optional 判斷要用 isPresent()，不能直接用 != null
        Optional<Order_travelers> traveler = orderTravelersRepository.findById(id);
        if (traveler.isPresent()) {
            orderTravelersRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public long getOrder_travelersCount() {
        return orderTravelersRepository.count();
    }

   // @Override
   // public void run(String... args) throws Exception {
    	
//    		if(orderTravelersRepository.count()==0) {
//    			orderTravelersRepository.save(new Order_travelers("John","john@demo.com","admin","09123456","1234","ACTIVE"));	
//		}
    	
//        if (orderTravelersRepository.count() == 0) {
//            // 修正 5：原先的參數完全對不上 Order_travelers 的欄位，且少了 Orders 物件。
//            // 這裡如果是要初始化假資料，必須先有一個有效的 Orders 物件，以下提供標準做法範例：      
//            Orders defaultOrder = ordersRepository.findById(1).orElse(null);
//            if (defaultOrder != null) {
//                Order_travelers sample = new Order_travelers();
//                sample.setOrders(defaultOrder);
//                sample.setTravelers_real_name("John");
//                sample.setTraveler_type("ADULT");
//                sample.setRoom_type("DOUBLE");
//                sample.setTraveler_status("ACTIVE");
//                orderTravelersRepository.save(sample);
//            }           
//        }
    	
    	
    	/* if (orderTravelersRepository.count() == 0) {

    	        Orders defaultOrder = ordersRepository.findById(1)
    	                .orElseThrow(() ->
    	                    new RuntimeException("找不到 order_id = 1 的訂單")
    	                );

    	        Order_travelers sample = new Order_travelers();

    	        sample.setOrders(defaultOrder);
    	        sample.setTravelers_real_name("John");
    	        sample.setTraveler_type("ADULT");
    	        sample.setRoom_type("DOUBLE");
    	        sample.setTraveler_status("ACTIVE");

    	        orderTravelersRepository.save(sample);

    	        System.out.println("成功建立預設旅客資料！");
    	        System.out.println("使用的 order_id = "+ defaultOrder.getOrder_id());
    	    }  
    }*/
        
      
}
