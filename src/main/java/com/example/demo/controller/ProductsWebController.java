package com.example.demo.controller;

import com.example.demo.model.Products;
import com.example.demo.service.ProductsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/web/products")
public class ProductsWebController {
    
    private final ProductsService ps;
    
    public ProductsWebController(ProductsService ps) {
        this.ps = ps;
    }
    
    // 顯示商品列表
    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", ps.getAllProducts());
        model.addAttribute("productsCount", ps.getProductsCount());
        return "products/list";
    }
    
    // 顯示商品詳情
    @GetMapping("/{product_id}")
    public String getProductsDetail(@PathVariable Integer product_id, Model model) {
        return ps.getProductsById(product_id)
                .map(product -> {
                    model.addAttribute("product", product);
                    return "products/detail";
                })
                .orElse("redirect:/web/products");
    }
    
    // 顯示建立表單
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new Products());
        model.addAttribute("isEdit", false);
        return "products/form";
    }
    
    // 處理建立表單
    @PostMapping("/create")
    public String createProducts(@ModelAttribute Products products, RedirectAttributes redirectAttributes) {
    	Products createdProducts = ps.createProduct(products.getName(),products.
				getAdult_double_price(),products.getAdult_single_price(), 
				products.getInfant_price(),products.getDeparture_airport(), 
				products.getDestination(), products.getMax_capacity(), 
				products.getAvailable_capacity(),products.getDescription(), 
				products.getNotification(),products.getProduct_status(), 
				products.getFly_day(), products.getBack_day()
				);

        redirectAttributes.addFlashAttribute("successMessage", "商品建立成功！");
        return "redirect:/web/products/" + createdProducts.getProduct_id();
    }
    
    // 顯示編輯表單
    @GetMapping("/{product_id}/edit")
    public String showEditForm(@PathVariable Integer product_id, Model model) {
        return ps.getProductsById(product_id)
                .map(product -> {
                    model.addAttribute("product", product);
                    model.addAttribute("isEdit", true);
                    return "products/form";
                })
                .orElse("redirect:/web/products");
    }
    
    // 處理編輯表單
    @PostMapping("/{product_id}/edit")
    public String updateProducts(@PathVariable("product_id") Integer product_id, 
    		@ModelAttribute Products products,RedirectAttributes redirectAttributes) {
    		ps.updateProducts(
    	        product_id,
    	        products.getName(),
    	        products.getAdult_double_price(),
    	        products.getAdult_single_price(),
    	        products.getInfant_price(),
    	        products.getDeparture_airport(),
    	        products.getDestination(),
    	        products.getMax_capacity(),
    	        products.getAvailable_capacity(),
    	        products.getDescription(),
    	        products.getNotification(),
    	        products.getProduct_status(),
    	        products.getFly_day(),
    	        products.getBack_day()
    	);
//    		ps.updateProducts(products.getProduct_id(),products.getName(),
//    				products.getAdult_double_price(),products.getAdult_single_price(), 
//    				products.getInfant_price(),products.getDeparture_airport(), 
//    				products.getDestination(), products.getMax_capacity(), 
//    				products.getAvailable_capacity(),products.getDescription(), 
//    				products.getNotification(),products.getProduct_status(), 
//    				products.getFly_day(), products.getBack_day()
//    				);
        redirectAttributes.addFlashAttribute("successMessage", "商品更新成功！");
        return "redirect:/web/products/" + product_id;
    }
    
    // 刪除商品
    @PostMapping("/{product_id}/delete")
    public String deleteProducts(@PathVariable("product_id") Integer product_id, 
    		RedirectAttributes redirectAttributes) {
        if (ps.deleteProducts(product_id)) {
            redirectAttributes.addFlashAttribute("successMessage", "商品刪除成功！");
        }
        return "redirect:/web/products";
    }
}