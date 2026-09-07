package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.Cart_items;
import com.example.demo.model.Users;
import com.example.demo.service.BrowseHistoryService;
import com.example.demo.service.OrdersService;
import com.example.demo.service.UserService;
import com.example.demo.service.CartItemsService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/web/dashboard")
public class DashboardController {

    private final UserService userService;
    private final OrdersService ordersService;
    private final CartItemsService cartitemsService;
    private final BrowseHistoryService browseHistoryService;
    public DashboardController(UserService userService,OrdersService ordersService,CartItemsService cartitemsService,
    		BrowseHistoryService browseHistoryService) {
        this.userService = userService;
        this.ordersService = ordersService;
        this.cartitemsService = cartitemsService;
        this.browseHistoryService = browseHistoryService;
    }

    @GetMapping
    public String dashboardHome(@ModelAttribute Users users,
            HttpSession session,
            Model model) {
    			try {
//    		try {Integer userId = userService.loginprocess(
//                users.getEmail(),
//                users.getPassword_hash());
    			Integer userId =(Integer) session.getAttribute("loginUserId");
    			if (userId == null) {
    				return "redirect:/web/memberlogin";
    			} 
    			if (!userId.equals(1)) {
    				return "redirect:/web/memberlogin";
    			}
    				return userService.getUserById(userId)
                .map(user -> {model.addAttribute("user", user);
                    return "dashboard/center";
                })
                .orElse("redirect:/web/memberlogin"); 
    		} catch (RuntimeException e) {
            model.addAttribute(
                    "errorMessage",
                    "信箱或密碼錯誤！"
            );

            Users loginUser = new Users();
            loginUser.setEmail(users.getEmail());

            model.addAttribute("user", loginUser);

            return "member/login";
        }
    
    }
    // 查看訂單
    @GetMapping("/orders")
    public String dashboardOrders(
            HttpSession session,
            Model model) {
        Integer userId =(Integer) session.getAttribute("loginUserId");
        if (userId == null) {
            return "redirect:/web/memberlogin";
        }
        model.addAttribute("orders", ordersService.getOrdersByUser_id(userId));
        model.addAttribute("ordersCount", ordersService.getOrdersCountByUser_id(userId));
        return "dashboard/ordersdetail";
    }   
    
    // 顯示瀏覽紀錄清單
    @GetMapping("/Browse_history")
    public String dashboardBrowse_history(
            HttpSession session,
            Model model) {
        Integer userId =(Integer) session.getAttribute("loginUserId");
        if (userId == null) {
            return "redirect:/web/memberlogin";
        }
        model.addAttribute("Browse_history", browseHistoryService.getByUser_id(userId));
        model.addAttribute("Browse_historyCount", browseHistoryService.getBrowse_historyCountByUser_id(userId));
        return "dashboard/bh";
    }
    
    // 在瀏覽紀錄當中加入購物車
    @GetMapping("/cart/create/{product_id}")
    public String cartProduct(
            @PathVariable Integer product_id,HttpSession session,Model model,RedirectAttributes redirectAttributes) {
        Integer userId = (Integer) session.getAttribute("loginUserId");
        if (userId == null) {
            session.setAttribute("cartProductId", product_id);
            return "redirect:/web/memberlogin";
        }
        Cart_items createdCart_items = cartitemsService.createCart_items(userId,product_id);
        redirectAttributes.addFlashAttribute("successMessage", "購物車內容建立成功！");
        return "redirect:/web/dashboard/Browse_history";
    }
    // 刪除購物車內容
    @PostMapping("/{cart_item_id}/delete")
    public String deleteCart_items(@PathVariable Integer cart_item_id, 
    		RedirectAttributes redirectAttributes) {
        if (cartitemsService.deleteCart_items(cart_item_id)) {
            redirectAttributes.addFlashAttribute("successMessage", "購物車內容刪除成功！");
        }
        return "redirect:/web/dashboard/cart";
    }
    
    @GetMapping("/editinfo")
    public String showEditForm(
            HttpSession session,
            Model model) {
        Integer userId =(Integer) session.getAttribute("loginUserId");
        if (userId == null) {
            return "redirect:/web/memberlogin";
        }
        return userService.getUserById(userId)
                .map(user -> {
                    model.addAttribute("user", user);
                    model.addAttribute("isEdit", true);
                    return "dashboard/register";
                })
                .orElse("redirect:/web/memberlogin");
    }
    
    @PostMapping("/editinfo")
    public String updateUser(
            @ModelAttribute Users users,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        Integer userId =(Integer) session.getAttribute("loginUserId");
        if (userId == null) {
            return "redirect:/web/memberlogin";
        }
        userService.updateUser(
                userId,
                users.getName(),
                //users.getEmail(),
                "dashboard",//users.getRole(),
                users.getPhone(),
                users.getPassword_hash(),
                users.getAccount_status()
        );
        redirectAttributes.addFlashAttribute("successMessage","會員資料更新成功！");
        return "redirect:/web/dashboard";
    }
    // 登出
    @GetMapping("/logout")
    public String dashboardlogout(HttpSession session) {

        session.invalidate();

        return "redirect:/web/memberlogin";
    }
}
