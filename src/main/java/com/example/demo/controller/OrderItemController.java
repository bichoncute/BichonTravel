package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.Order_item;
import com.example.demo.model.Orders;
import com.example.demo.model.Products;
import com.example.demo.service.OrderItemService;

@Controller
@RequestMapping("/web/order_item")
public class OrderItemController {
	private final OrderItemService orderItemService;
    
	 public OrderItemController(OrderItemService orderItemService) {
	        this.orderItemService = orderItemService;
	    }  
    
    // 顯示訂單列表
    @GetMapping
    public String listOrder_item(Model model) {
        model.addAttribute("order_item", orderItemService.getAllOrder_items());
        model.addAttribute("order_itemsCount", orderItemService.getOrder_itemsCount());
        return "order_item/list";
    }
    
    // 前台顯示訂單明細詳情
    @GetMapping("/{order_item_id}")
    public String getOrder_itemDetail(@PathVariable Integer order_item_id, Model model) {
        return orderItemService.getOrder_itemById(order_item_id)
                .map(order_item -> {
                    model.addAttribute("order_item", order_item);
                    //return "order_item/detail";
                    return "member/orderitemdetail";
                })
                .orElse("redirect:/web/order_item");
    }
    // 後台顯示訂單明細詳情
    @GetMapping("/dashboard/{order_item_id}")
    public String dashboardgetOrder_itemDetail(@PathVariable Integer order_item_id, Model model) {
        return orderItemService.getOrder_itemById(order_item_id)
                .map(order_item -> {
                    model.addAttribute("order_item", order_item);
                    return "order_item/detail";
                    //return "member/orderitemdetail";
                })
                .orElse("redirect:/web/order_item");
    }
   // 後台顯示訂單明細詳情
    @GetMapping("/detail/{order_id}")
    public String getOrder_itemDetailbyorderid(@PathVariable Integer order_id, Model model) {
        return orderItemService.getOrder_itemByOrderId(order_id)
                .map(order_item -> {
                    model.addAttribute("order_item", order_item);
                    return "order_item/detail";
                })
                .orElse("redirect:/web/order_item");
    }
    
    // 顯示建立表單
    @GetMapping("/create/{order_id}/{product_id}")
    public String showCreateForm(
            @PathVariable Integer order_id,
            @PathVariable Integer product_id,
            Model model) {

        Order_item order_item = new Order_item();

        Orders order = new Orders();
        order.setOrder_id(order_id);
        order_item.setOrders(order);

        Products product = new Products();
        product.setProduct_id(product_id);
        order_item.setProducts(product);

        model.addAttribute("order_item", order_item);
        model.addAttribute("isEdit", false);

        return "order_item/form";
    }

    // 處理建立表單
    @PostMapping("/create/{order_id}/{product_id}")
    public String createOrder_item(
            @PathVariable Integer order_id,
            @PathVariable Integer product_id,
            @ModelAttribute Order_item order_item,
            RedirectAttributes redirectAttributes) {
    			Order_item createdOrder_item =
    	        orderItemService.createOrder_items(
    	                order_id,
    	                product_id,
    	                order_item.getAdult_double_qty(),
    	                order_item.getAdult_single_qty(),
    	                order_item.getInfant_qty()
    	        );

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "訂單明細建立成功！"
        );

        return "redirect:/web/order_item/"
                + createdOrder_item.getOrder_item_id();
    }
    
    // 顯示編輯表單
    @GetMapping("/{order_item_id}/edit")
    public String showEditForm(@PathVariable Integer order_item_id, Model model) {
        return orderItemService.getOrder_itemById(order_item_id)
                .map(order_item -> {
                    model.addAttribute("order_item", order_item);
                    model.addAttribute("isEdit", true);
                    return "order_item/form";
                })
                .orElse("redirect:/web/order_item");
    }
    
    // 處理編輯表單
    @PostMapping("/{order_item_id}/edit")
    public String updateOrder_item(@PathVariable Integer order_item_id, 
    		//@PathVariable Integer product_id,
    		@ModelAttribute Order_item order_item,
    		RedirectAttributes redirectAttributes) {
    	
    		Integer orderId = (order_item.getOrders() != null) ? 
    				order_item.getOrders().getOrder_id() : null;
        if (orderId == null) {
            throw new RuntimeException("編輯時必須指定訂單 ID");
        }     
        Integer productId = (order_item.getProducts() != null) ? order_item.getProducts().getProduct_id() : null;
		if (productId == null) {
			throw new RuntimeException("編輯時必須指定商品 ID");
		}       
        orderItemService.updateOrder_items(order_item_id, orderId, productId,
        		 order_item.getAdult_double_qty(),
                 order_item.getAdult_single_qty(),
                 order_item.getInfant_qty()
    	);
        redirectAttributes.addFlashAttribute("successMessage", "訂單明細更新成功！");
        return "redirect:/web/order_item/dashboard/" + order_item_id;
    }
    
    // 刪除訂單明細
    @PostMapping("/{order_item_id}/delete")
    public String deleteOrder_item(@PathVariable Integer order_item_id, 
    		RedirectAttributes redirectAttributes) {
        if (orderItemService.deleteOrder_items(order_item_id)) {
            redirectAttributes.addFlashAttribute("successMessage", "訂單明細刪除成功！");
        }
        return "redirect:/web/order_item";
    }
}
