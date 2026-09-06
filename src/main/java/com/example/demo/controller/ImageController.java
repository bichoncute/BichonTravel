package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.Image;
import com.example.demo.model.Products;
import com.example.demo.service.ImageService;

@Controller
@RequestMapping("/web/image")
public class ImageController {
	private final ImageService imageService;
    
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }
    
    // 顯示圖片列表
    @GetMapping
    public String listImage(Model model) {
        model.addAttribute("image", imageService.getAllImage());
        model.addAttribute("imageCount", imageService.getImageCount());
        return "image/list";
    }
    
    // 顯示圖片詳情
    @GetMapping("/{image_id}")
    public String getImageDetail(@PathVariable Integer image_id, Model model) {
        return imageService.getImageById(image_id)
                .map(image -> {
                    model.addAttribute("image", image);
                    return "image/detail";
                })
                .orElse("redirect:/web/image");
    }
    
    // 顯示圖片表單
    @GetMapping("/create/{product_id}")
    public String showCreateForm(@PathVariable Integer product_id, Model model) {  	
    	Image image = new Image();
        Products products = new Products();
        products.setProduct_id(product_id);
        image.setProducts(products);
        model.addAttribute("image", image);
        model.addAttribute("isEdit", false);
        return "image/form";
    }
    
 // 處理建立表單
    @PostMapping("/create/{product_id}")
    public String createImage(
            @PathVariable Integer product_id,
            @ModelAttribute Image image,
            RedirectAttributes redirectAttributes) {
    		Image createdImage =
    					imageService.createImage(
    							product_id,
    							image.getImage_name(),
    							image.getImage_url()
                );
        redirectAttributes.addFlashAttribute(
                "successMessage",
                "圖片建立成功！"
        );
        return "redirect:/web/image/"
                + createdImage.getImage_id();
    }
    
    // 顯示編輯表單
    @GetMapping("/{image_id}/edit")
    public String showEditForm(@PathVariable Integer image_id, Model model) {
        return imageService.getImageById(image_id)
                .map(image -> {
                    model.addAttribute("image", image);
                    model.addAttribute("isEdit", true);
                    return "image/form";
                })
                .orElse("redirect:/web/image");
    }
    
    // 處理編輯表單
    @PostMapping("/{image_id}/edit")
    public String updateImage(@PathVariable Integer image_id, 
    		@ModelAttribute Image image,
    		RedirectAttributes redirectAttributes) {
    	
    		Integer productId = (image.getProducts() != null) ? 
    				image.getProducts().getProduct_id() : null;
        if (productId == null) {
            throw new RuntimeException("編輯時必須指定產品 ID");
        }
        
        imageService.updateImage(image_id, productId, 
        		image.getImage_name(), image.getImage_url()	);
        redirectAttributes.addFlashAttribute("successMessage", "圖片更新成功！");
        return "redirect:/web/image/" + image_id;
    }
    
    // 刪除圖片
    @PostMapping("/{image_id}/delete")
    public String deleteImage(@PathVariable Integer image_id, RedirectAttributes redirectAttributes) {
        if (imageService.deleteImage(image_id)) {
            redirectAttributes.addFlashAttribute("successMessage", "圖片刪除成功！");
        }
        return "redirect:/web/image";
    }
}
