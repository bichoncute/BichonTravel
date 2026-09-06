package com.example.demo.controller;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;

import com.example.demo.model.Cart_items;
import com.example.demo.model.Order_item;
import com.example.demo.model.Orders;
import com.example.demo.model.Products;
import com.example.demo.model.Users;
import com.example.demo.service.ProductsService;
import com.example.demo.service.OrdersService;
import com.example.demo.service.BrowseHistoryService;
import com.example.demo.service.CartItemsService;
import com.example.demo.service.OrderItemService;
@Controller
@RequestMapping("/web/frontproducts")
public class ProductsFrontController {
	private final ProductsService ps;
	private final OrdersService ordersService;
	private final OrderItemService orderItemService;
	private final CartItemsService  cartItemsService;
    private final BrowseHistoryService browseHistoryService;
	public ProductsFrontController(
	        ProductsService ps,
	        OrdersService ordersService,
	        OrderItemService orderItemService,CartItemsService  cartItemsService,
	        BrowseHistoryService browseHistoryService) {

	    this.ps = ps;
	    this.ordersService = ordersService;
	    this.orderItemService = orderItemService;
	    this.cartItemsService = cartItemsService;
	    this.browseHistoryService = browseHistoryService;
	}
    
    // 顯示商品列表
    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", ps.getAllProducts());
        model.addAttribute("productsCount", ps.getProductsCount());
        return "frontproducts/list";
    }
    @GetMapping("/search")
    public String searchProducts(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            Model model) {

        List<Products> products =
                ps.searchByFly_day(startDate, endDate);

        model.addAttribute("products", products);
        model.addAttribute("productsCount", products.size());

        return "frontproducts/list";
    }
    // 顯示商品詳情
    @GetMapping("/{product_id}")
    public String getProductsDetail(@PathVariable Integer product_id, HttpSession session, Model model) {
    	 // 如果有登入，就記錄瀏覽紀錄
        Integer userId =
                (Integer) session.getAttribute("loginUserId"); 
        if (userId != null) {

            browseHistoryService.createBrowse_history(
                    userId,
                    product_id
            );
        }
        return ps.getProductsById(product_id)
                .map(product -> {
                    model.addAttribute("product", product);
                    return "frontproducts/detail";
                })
                .orElse("redirect:/web/frontproducts");
    }
    
    @GetMapping("/buy/{product_id}")
    public String buyProduct(
            @PathVariable Integer product_id,
            HttpSession session,
            Model model) {

        Integer userId = (Integer) session.getAttribute("loginUserId");

        if (userId == null) {

            session.setAttribute(
                    "buyProductId",
                    product_id
            );

            return "redirect:/web/memberlogin";
        }
//        // 輸入數量超過ㄎ
//        if (userId == 0) {
//            model.addAttribute(
//                    "errorMessage",
//                    "密碼錯誤！或帳號已被刪除"
//            );           
//            // 保留 Email
//            Users loginUser = new Users();
//            loginUser.setEmail(users.getEmail());
//            model.addAttribute("user", loginUser);
//            return "member/login";
//        }
        return ps.getProductsById(product_id)
                .map(product -> {
                    model.addAttribute("product", product);
                    return "frontproducts/buy";
                })
                .orElse("redirect:/web/frontproducts");
    }
    
    // 加入購物車
    @GetMapping("/cart/create/{product_id}")
    public String cartProduct(
            @PathVariable Integer product_id,HttpSession session,Model model,RedirectAttributes redirectAttributes) {
        Integer userId = (Integer) session.getAttribute("loginUserId");
        if (userId == null) {
            session.setAttribute("cartProductId", product_id);
            return "redirect:/web/memberlogin";
        }
        Cart_items createdCart_items = cartItemsService.createCart_items(userId,product_id);
        redirectAttributes.addFlashAttribute("successMessage", "購物車內容建立成功！");
        return "redirect:/web/frontproducts";
    }
    
    // 處理建立表單
    @PostMapping("/create/{user_id}/{product_id}")
    public String createCart_items(
            @PathVariable Integer user_id,
            @PathVariable Integer product_id,
            @ModelAttribute Cart_items cart_items,
            RedirectAttributes redirectAttributes) {
  	  		Cart_items createdCart_items =
  	   		cartItemsService.createCart_items(
    					user_id,
    	                product_id
    	        );
        redirectAttributes.addFlashAttribute(
                "successMessage",
                "購物車內容建立成功！"
        );
        return "redirect:/web/cart_items/"
                + createdCart_items.getCart_item_id();
    } 
    
    @PostMapping("/buy/{product_id}")
    public String buyProductConfirm(
            @PathVariable Integer product_id,
            @RequestParam Integer adult_double_qty,
            @RequestParam Integer adult_single_qty,
            @RequestParam Integer infant_qty,

            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Integer user_id =
                (Integer) session.getAttribute("loginUserId");

        if (user_id == null) {
            return "redirect:/web/memberlogin";
        }

        if (adult_double_qty < 0 ||
            adult_single_qty < 0 ||
            infant_qty < 0) {

            throw new RuntimeException("數量不能小於 0");
        }

        if (adult_double_qty
                + adult_single_qty
                + infant_qty <= 0) {

            throw new RuntimeException("至少需要選擇一位旅客");
        }

        // 1. 建立 Orders
        Orders orders = ordersService.createOrders(
                user_id,
                "UNPAID",
                0
        );

        // 2. 建立 Order_item
        Order_item order_item =
                orderItemService.createOrder_items(
                        orders.getOrder_id(),
                        product_id,
                        adult_double_qty,
                        adult_single_qty,
                        infant_qty
                );

        // 3. 計算總人數
        Integer reserved_quantity =
                adult_double_qty
                + adult_single_qty
                + infant_qty;

        // 4. 更新 Orders
        orders.setTotal_amount(order_item.getSubtotal());
        orders.setReserved_quantity(reserved_quantity);

        ordersService.updateOrders(
                orders.getOrder_id(),
                user_id,
                order_item.getSubtotal(),
                "UNPAID",
                reserved_quantity
        );

        // 如果這個商品原本在會員的願望清單中，
        // 確認購買後就將它從願望清單移除
        cartItemsService.deleteByUserAndProduct(
                user_id,
                product_id
        );

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "訂單建立成功！"
        );

        return "redirect:/web/order_item/"
                + order_item.getOrder_item_id();
    }
}
