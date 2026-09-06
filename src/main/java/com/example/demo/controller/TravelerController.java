package com.example.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.Order_travelers;
import com.example.demo.model.Orders;
import com.example.demo.service.TravelerService;


@Controller
@RequestMapping("/web/order_travelers")
public class TravelerController {
	private final TravelerService travelerService;
    
    public TravelerController(TravelerService travelerService) {
        this.travelerService = travelerService;
    }   
    
    // 顯示旅客列表
    @GetMapping
    public String listOrder_travelers(Model model) {
    	model.addAttribute("isDash", false);
        model.addAttribute("order_travelers", travelerService.getAllOrder_travelers());
        model.addAttribute("order_travelersCount", travelerService.getOrder_travelersCount());
        return "ordertravelers/list";
    }
    // 後台顯示旅客列表，沒有前往付款按鈕
    @GetMapping("/dash")
    public String listOrder_travelers_dashboard(Model model) {
    	model.addAttribute("isDash", true);
        model.addAttribute("order_travelers", travelerService.getAllOrder_travelers());
        model.addAttribute("order_travelersCount", travelerService.getOrder_travelersCount());
        return "ordertravelers/list";
    }
    
    @GetMapping("/order/{order_id}")
    public String listOrderTravelersByOrder(
            @PathVariable Integer order_id,
            Model model) {

        List<Order_travelers> travelers =
                travelerService.getOrderTravelersByOrderId(order_id);
        model.addAttribute("order_id", order_id);
        model.addAttribute("order_travelers", travelers);
        model.addAttribute("order_travelersCount", travelers.size());

        return "ordertravelers/list";
    }
    
   
    // 顯示旅客詳情
    @GetMapping("/{travelers_id}")
    public String getOrder_travelersDetail(@PathVariable Integer travelers_id, Model model) {
        return travelerService.getOrder_travelersById(travelers_id)
                .map(order_travelers -> {
                    model.addAttribute("order_travelers", order_travelers);
                    return "ordertravelers/detail";
                })
                .orElse("redirect:/web/order_travelers");
    }
    
    // 顯示建立表單
    @GetMapping("/create/{order_id}")
    public String showCreateForm(@PathVariable Integer order_id, Model model) {

        Order_travelers traveler = new Order_travelers();

        Orders order = new Orders();
        order.setOrder_id(order_id);

        traveler.setOrders(order);

        model.addAttribute("order_travelers", traveler);
        model.addAttribute("isEdit", false);

        return "ordertravelers/form";
    }
    
    // 處理建立表單
    @PostMapping("/create/{order_id}")
    public String createTravelers(
            @PathVariable Integer order_id,
            @ModelAttribute Order_travelers order_travelers,
            RedirectAttributes redirectAttributes) {

        Order_travelers createdOrderTravelers =
                travelerService.createOrder_travelers(
                        order_id,
                        order_travelers.getTravelers_real_name(),
                        order_travelers.getPassport_num(),
                        order_travelers.getPhone_num(),
                        order_travelers.getLine_account(),
                        order_travelers.getSpecial_request(),
                        order_travelers.getTraveler_type(),
                        order_travelers.getRoom_type(),
                        order_travelers.getTraveler_status()
                );

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "使用者建立成功！"
        );
        return "redirect:/web/order_travelers/"
                + createdOrderTravelers.getTravelers_id();
    }  
    
    // 顯示編輯表單
    @GetMapping("/{travelers_id}/edit")
    public String showEditForm(@PathVariable Integer travelers_id, Model model) {
        return travelerService.getOrder_travelersById(travelers_id)
                .map(order_travelers -> {
                    model.addAttribute("order_travelers", order_travelers);
                    model.addAttribute("isEdit", true);
                    return "ordertravelers/form";
                })
                .orElse("redirect:/web/order_travelers");
    }
    
    // 處理編輯表單
    @PostMapping("/{travelers_id}/edit")
    public String updateTravelers(@PathVariable Integer travelers_id, 
    		@ModelAttribute Order_travelers order_travelers,
    		RedirectAttributes redirectAttributes) {
    	
    		Integer orderId = (order_travelers.getOrders() != null) ? 
    				order_travelers.getOrders().getOrder_id() : null;
        if (orderId == null) {
            throw new RuntimeException("編輯時必須指定訂單 ID");
        }
        
    		travelerService.updateOrder_travelers(travelers_id, orderId,
    			order_travelers.getTravelers_real_name(), 
    			order_travelers.getPassport_num(), order_travelers.getPhone_num(),
    			order_travelers.getLine_account(), order_travelers.getSpecial_request(),
    			order_travelers.getTraveler_type(), order_travelers.getRoom_type(),
    			order_travelers.getTraveler_status());
        redirectAttributes.addFlashAttribute("successMessage", "使用者更新成功！");
        return "redirect:/web/order_travelers/" + travelers_id;
    }
    
    // 刪除旅客
    @PostMapping("/{id}/delete")
    public String deleteUser(
            @PathVariable Integer id,
            RedirectAttributes redirectAttributes) {

        Order_travelers traveler =
                travelerService.getOrder_travelersById(id)
                        .orElseThrow(() ->
                                new RuntimeException("找不到旅客 ID: " + id));

        Integer orderId =
                traveler.getOrders().getOrder_id();

        if (travelerService.deleteOrder_travelers(id)) {

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "旅客刪除成功！");
        }

        return "redirect:/web/order_travelers/order/" + orderId;
    }
}
