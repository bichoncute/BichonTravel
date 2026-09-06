package com.example.demo.controller;

import java.util.Optional;

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
@RequestMapping("/web/member")
public class MemberWebController {

    private final UserService userService;
    private final OrdersService ordersService;
    private final CartItemsService cartitemsService;
    private final BrowseHistoryService browseHistoryService;
    public MemberWebController(UserService userService,OrdersService ordersService,CartItemsService cartitemsService,
    		BrowseHistoryService browseHistoryService) {
        this.userService = userService;
        this.ordersService = ordersService;
        this.cartitemsService = cartitemsService;
        this.browseHistoryService = browseHistoryService;
    }

    @GetMapping
    public String memberHome(
            HttpSession session,
            Model model) {
    	
        Integer userId =(Integer) session.getAttribute("loginUserId");
        if (userId == null) {
            return "redirect:/web/memberlogin";
        }

        return userService.getUserById(userId)
                .map(user -> {model.addAttribute("user", user);
                    return "member/center";
                })
                .orElse("redirect:/web/memberlogin");
    }
    // 查看訂單
    @GetMapping("/orders")
    public String memberOrders(
            HttpSession session,
            Model model) {
        Integer userId =(Integer) session.getAttribute("loginUserId");
        if (userId == null) {
            return "redirect:/web/memberlogin";
        }
        model.addAttribute("orders", ordersService.getOrdersByUser_id(userId));
        model.addAttribute("ordersCount", ordersService.getOrdersCountByUser_id(userId));
        return "member/ordersdetail";
    }
    
    // 顯示購物車清單
    @GetMapping("/cart")
    public String memberCarts(
            HttpSession session,
            Model model) {
        Integer userId =(Integer) session.getAttribute("loginUserId");
        if (userId == null) {
            return "redirect:/web/memberlogin";
        }
        model.addAttribute("cart", cartitemsService.getByUser_id(userId));
        model.addAttribute("cartCount", cartitemsService.getCartsCountByUser_id(userId));
        return "member/cart";
    }
    
    // 顯示瀏覽紀錄清單
    @GetMapping("/Browse_history")
    public String memberBrowse_history(
            HttpSession session,
            Model model) {
        Integer userId =(Integer) session.getAttribute("loginUserId");
        if (userId == null) {
            return "redirect:/web/memberlogin";
        }
        model.addAttribute("Browse_history", browseHistoryService.getByUser_id(userId));
        model.addAttribute("Browse_historyCount", browseHistoryService.getBrowse_historyCountByUser_id(userId));
        return "member/bh";
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
        return "redirect:/web/member/Browse_history";
    }
    // 刪除購物車內容
    @PostMapping("/{cart_item_id}/delete")
    public String deleteCart_items(@PathVariable Integer cart_item_id, 
    		RedirectAttributes redirectAttributes) {
        if (cartitemsService.deleteCart_items(cart_item_id)) {
            redirectAttributes.addFlashAttribute("successMessage", "購物車內容刪除成功！");
        }
        return "redirect:/web/member/cart";
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
                    model.addAttribute("isUpdate", true);
                    model.addAttribute("user", user);
                    model.addAttribute("isEdit", true);
                    return "member/register";
                })
                .orElse("redirect:/web/memberlogin");
    }
    
    @PostMapping("/editinfo")
    public String updateUser(
            @ModelAttribute Users users,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {
        Integer userId =(Integer) session.getAttribute("loginUserId");
        if (userId == null) {
            return "redirect:/web/member/editinfo";
        }
//        Optional<Users> user = userService.getUserById(userId);
//        String useremail = 
      
        		
		    		userService.updateUser(
                userId,
                users.getName(),
                //users.getEmail(),
                "member",//users.getRole(),
                users.getPhone(),
                users.getPassword_hash(),
                users.getAccount_status()
        );
        redirectAttributes.addFlashAttribute("successMessage","會員資料更新成功！");
        return "redirect:/web/member";
        
    }
    // 登出
    @GetMapping("/logout")
    public String memberlogout(HttpSession session) {

        session.invalidate();

        return "redirect:/web/memberlogin";
    }
}
