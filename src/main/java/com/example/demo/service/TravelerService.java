package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Order_travelers;
import com.example.demo.model.Orders;
import com.example.demo.repository.OrderTravelersRepository;
import com.example.demo.repository.OrdersRepository; 

@Service
public class TravelerService {

    @Autowired
    private OrderTravelersRepository orderTravelersRepository;

    @Autowired
    private OrdersRepository ordersRepository; 
    public Order_travelers createOrder_travelers(Integer order_id, String travelers_real_name,
            String passport_num, String phone_num, String line_account,
            String special_request, String traveler_type, String room_type,
            String traveler_status) {

        Orders orders = ordersRepository.findById(order_id)
                .orElseThrow(() -> new RuntimeException("找不到對應的訂單 ID: " + order_id));

        Order_travelers order_travelers = new Order_travelers();
        order_travelers.setOrders(orders);
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

        Optional<Order_travelers> existingOrder_travelers = orderTravelersRepository.findById(travelers_id);

        if (existingOrder_travelers.isPresent()) {
            Order_travelers order_travelers = existingOrder_travelers.get();
            Orders orders = ordersRepository.findById(order_id)
                    .orElseThrow(() -> new RuntimeException("找不到對應的訂單 ID: " + order_id));

            order_travelers.setTravelers_id(travelers_id);
            order_travelers.setOrders(orders); 
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
        throw new RuntimeException("使用者不存在: " + travelers_id); 
    }

    public boolean deleteOrder_travelers(Integer id) {
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
}
