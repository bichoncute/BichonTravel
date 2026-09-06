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
import com.example.demo.model.Products;
import com.example.demo.model.Users;
import com.example.demo.service.CartItemsService;
import com.example.demo.service.ProductsService;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/web/cart_items")
public class CartItemsController {
	private final CartItemsService cartItemsService;
    private final UserService userService;
    private final ProductsService productservice;
	 public CartItemsController(CartItemsService cartItemsService,ProductsService productservice,
			 UserService userService) {
	        this.cartItemsService = cartItemsService;
	        this.productservice = productservice;
	        this.userService = userService;
	    }  
  
  // 顯示列表
  @GetMapping
  public String listCart_items(HttpSession session,Model model) {
	  Integer userId =(Integer) session.getAttribute("loginUserId");
      if (userId == null) {
          return "redirect:/web/memberlogin";
      }

      model.addAttribute("cart_items", cartItemsService.getAllCart_items());
      model.addAttribute("cart_itemsCount", cartItemsService.getCart_itemsCount());
      return "cart_items/list";
  }
  
  // 顯示購物車內容詳情
  @GetMapping("/{cart_item_id}")
  public String getCart_itemsDetail(@PathVariable Integer cart_item_id, Model model,HttpSession session) {
      return cartItemsService.getCart_itemsById(cart_item_id)
              .map(cart_items -> {
                  model.addAttribute("cart_items", cart_items);
                  return "cart_items/detail";
              })
              .orElse("redirect:/web/cart_items");
  }
  
  // 顯示建立表單
  @GetMapping("/create/{user_id}/{product_id}")
  public String showCreateForm(
          @PathVariable Integer user_id,
          @PathVariable Integer product_id,
          Model model) {
	  Cart_items cart_items = new Cart_items();
      Users users = new Users();
      users.setId(user_id);
      cart_items.setUsers(users);

      Products product = new Products();
      product.setProduct_id(product_id);
      cart_items.setProducts(product);

      model.addAttribute("cart_items", cart_items);
      model.addAttribute("isEdit", false);

      return "cart_items/form";
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
  // 顯示編輯表單
  @GetMapping("/{cart_item_id}/edit")
  public String showEditForm(@PathVariable Integer cart_item_id, Model model) {
      return cartItemsService.getCart_itemsById(cart_item_id)
              .map(cart_items -> {
                  model.addAttribute("cart_items", cart_items);
                  model.addAttribute("isEdit", true);
                  return "cart_items/form";
              })
              .orElse("redirect:/web/cart_items");
  }
  
  // 處理編輯表單
  @PostMapping("/{cart_item_id}/edit")
  public String updateCart_items(@PathVariable Integer cart_item_id, 
  		//@PathVariable Integer product_id,
  		@ModelAttribute Cart_items cart_items,
  		RedirectAttributes redirectAttributes) {   	
  		Integer userId = (cart_items.getUsers() != null) ? 
  				cart_items.getUsers().getId() : null;
      if (userId == null) {
          throw new RuntimeException("編輯時必須指定使用者 ID");
      }     
      Integer productId = (cart_items.getProducts() != null) ? 
    		  cart_items.getProducts().getProduct_id() : null;
		if (productId == null) {
			throw new RuntimeException("編輯時必須指定商品 ID");
		}       
		cartItemsService.updateCart_items(cart_item_id, userId, productId);
      redirectAttributes.addFlashAttribute("successMessage", "購物車內容更新成功！");
      return "redirect:/web/cart_items/" + cart_item_id;
  }
  
  // 刪除購物車內容
  @PostMapping("/{cart_item_id}/delete")
  public String deleteCart_items(@PathVariable Integer cart_item_id, 
  		RedirectAttributes redirectAttributes) {
      if (cartItemsService.deleteCart_items(cart_item_id)) {
          redirectAttributes.addFlashAttribute("successMessage", "購物車內容刪除成功！");
      }
      return "redirect:/web/cart_items";
  }
}
