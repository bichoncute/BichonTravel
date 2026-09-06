package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.demo.model.Orders;
import com.example.demo.model.Users;
import com.example.demo.service.OrdersService;

@Controller
@RequestMapping("/web/orders")
public class OrdersController {
private final OrdersService ordersService;
    
    public OrdersController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }
    
    // 顯示訂單列表
    @GetMapping
    public String listOrders(Model model) {
        model.addAttribute("orders", ordersService.getAllOrders());
        model.addAttribute("ordersCount", ordersService.getOrdersCount());
        return "orders/list";
    }
    
    // 顯示訂單詳情
    @GetMapping("/{order_id}")
    public String getOrdersDetail(@PathVariable Integer order_id, Model model) {
        return ordersService.getOrdersById(order_id)
                .map(orders -> {
                    model.addAttribute("orders", orders);
                    return "orders/detail";
                })
                .orElse("redirect:/web/orders");
    }
    
    // 顯示訂單表單
    @GetMapping("/create/{user_id}")
    public String showCreateForm(@PathVariable Integer user_id, Model model) {  	
    		Orders orders = new Orders();
        Users users = new Users();
        users.setId(user_id);
        orders.setUsers(users);
        model.addAttribute("orders", orders);
        model.addAttribute("isEdit", false);
        return "orders/form";
    }
    
    // 處理建立表單
    @PostMapping("/create/{user_id}")
    public String createOrder(
            @PathVariable Integer user_id,
            @ModelAttribute Orders orders,
            RedirectAttributes redirectAttributes) {
    			Orders createdOrders =
    					ordersService.createOrders(
                        user_id,
                        //orders.getTotal_amount(),
                        orders.getOrder_status(),
                        orders.getReserved_quantity()
                );
        redirectAttributes.addFlashAttribute(
                "successMessage",
                "訂單建立成功！"
        );
        return "redirect:/web/orders/"
                + createdOrders.getOrder_id();
    }
    
    // 顯示編輯表單
    @GetMapping("/{order_id}/edit")
    public String showEditForm(@PathVariable Integer order_id, Model model) {
        return ordersService.getOrdersById(order_id)
                .map(orders -> {
                    model.addAttribute("orders", orders);
                    model.addAttribute("isEdit", true);
                    return "orders/form";
                })
                .orElse("redirect:/web/orders");
    }
    
    // 處理編輯表單
    @PostMapping("/{order_id}/edit")
    public String updateOrders(@PathVariable Integer order_id, 
    		@ModelAttribute Orders orders,
    		RedirectAttributes redirectAttributes) {
    	
    		Integer userId = (orders.getUsers() != null) ? 
    				orders.getUsers().getId() : null;
        if (userId == null) {
            throw new RuntimeException("編輯時必須指定使用者 ID");
        }
        
        ordersService.updateOrders(order_id, userId, orders.getTotal_amount(),
    			orders.getOrder_status(), orders.getReserved_quantity()	);
        redirectAttributes.addFlashAttribute("successMessage", "訂單更新成功！");
        return "redirect:/web/orders/" + order_id;
    }
    
    // 刪除訂單
    @PostMapping("/{order_id}/delete")
    public String deleteOrders(@PathVariable Integer order_id, RedirectAttributes redirectAttributes) {
        if (ordersService.deleteOrders(order_id)) {
            redirectAttributes.addFlashAttribute("successMessage", "訂單刪除成功！");
        }
        return "redirect:/web/orders";
    }
}
