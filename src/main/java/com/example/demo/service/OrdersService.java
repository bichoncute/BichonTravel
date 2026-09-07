package com.example.demo.service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.model.Orders;
import com.example.demo.model.Users;
import com.example.demo.repository.OrdersRepository;
import com.example.demo.repository.UserRepository;

@Service
public class OrdersService {
	@Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private UserRepository userRepository; 

    public Orders createOrders(Integer user_id, //BigDecimal total_amount,
    		String order_status, Integer reserved_quantity) {

    		Users users = userRepository.findById(user_id)
                .orElseThrow(() -> new RuntimeException("找不到對應的使用者 ID: " + user_id));

        Orders orders = new Orders();
        orders.setUsers(users);
        orders.setTotal_amount(BigDecimal.ZERO);
        orders.setOrder_status(order_status);
        orders.setReserved_quantity(reserved_quantity);

        return ordersRepository.save(orders);
    }

    public Optional<Orders> getOrdersById(Integer order_id) {
        return ordersRepository.findById(order_id);
    }

    public List<Orders> getAllOrders() {
        return ordersRepository.findAll();
    }
    
    public List<Orders> getOrdersByUser_id(Integer user_id) {
        return ordersRepository.findByUser_id(user_id);
    }

    public Integer getOrdersCountByUser_id(Integer user_id) {
        return ordersRepository.getOrdersCountByUser_id(user_id);
    }
    
    public Orders updateOrders(
            Integer order_id,
            Integer user_id,
            BigDecimal total_amount,
            String order_status,
            Integer reserved_quantity) {

        Optional<Orders> existingOrders =
                ordersRepository.findById(order_id);

        if (existingOrders.isPresent()) {

            Orders orders = existingOrders.get();

            Users users = userRepository.findById(user_id)
                    .orElseThrow(() ->
                        new RuntimeException(
                            "找不到對應的使用者 ID: " + user_id
                        )
                    );

            orders.setUsers(users);

            orders.setTotal_amount(total_amount);

            orders.setOrder_status(order_status);

            orders.setReserved_quantity(reserved_quantity);

            return ordersRepository.save(orders);
        }

        throw new RuntimeException(
            "訂單不存在: " + order_id
        );
    }

    public boolean deleteOrders(Integer order_id) {
        Optional<Orders> found_orders = ordersRepository.findById(order_id);
        if (found_orders.isPresent()) {
        		Orders orders = found_orders.get();
        		orders.setOrder_status("Cancelled");
        		ordersRepository.save(orders);
        
            return true;
        }
        return false;
    }

    public long getOrdersCount() {
        return ordersRepository.count();
    }
}
