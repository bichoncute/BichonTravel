package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.Browse_history;
import com.example.demo.model.Products;
import com.example.demo.model.Users;
import com.example.demo.service.BrowseHistoryService;

@Controller
@RequestMapping("/web/browse_history")
public class BrowseHistoryController {
	private final BrowseHistoryService browseHistoryService;
    
	 public BrowseHistoryController(BrowseHistoryService browseHistoryService) {
	        this.browseHistoryService = browseHistoryService;
	    }  
   
   // 顯示列表
   @GetMapping
   public String listBrowse_history(Model model) {
       model.addAttribute("browse_history", browseHistoryService.getAllBrowse_history());
       model.addAttribute("browse_historyCount", browseHistoryService.getBrowse_historyCount());
       return "browse_history/list";
   }
   
   // 顯示瀏覽紀錄詳情
   @GetMapping("/{bh_id}")
   public String getBrowse_historyDetail(@PathVariable Integer bh_id, Model model) {
       return browseHistoryService.getBrowse_historyById(bh_id)
               .map(browse_history -> {
                   model.addAttribute("browse_history", browse_history);
                   return "browse_history/detail";
               })
               .orElse("redirect:/web/browse_history");
   }
   
   // 顯示建立表單
   @GetMapping("/create/{user_id}/{product_id}")
   public String showCreateForm(
           @PathVariable Integer user_id,
           @PathVariable Integer product_id,
           Model model) {
	   Browse_history browse_history = new Browse_history();
       Users users = new Users();
       users.setId(user_id);
       browse_history.setUsers(users);

       Products product = new Products();
       product.setProduct_id(product_id);
       browse_history.setProducts(product);

       model.addAttribute("browse_history", browse_history);
       model.addAttribute("isEdit", false);

       return "browse_history/form";
   }

   // 處理建立表單
   @PostMapping("/create/{user_id}/{product_id}")
   public String createBrowse_history(
           @PathVariable Integer user_id,
           @PathVariable Integer product_id,
           @ModelAttribute Browse_history browse_history,
           RedirectAttributes redirectAttributes) {
	   		Browse_history createdBrowse_history =
   			browseHistoryService.createBrowse_history(
   					user_id,
   	                product_id
   	        );
       redirectAttributes.addFlashAttribute(
               "successMessage",
               "瀏覽紀錄建立成功！"
       );
       return "redirect:/web/browse_history/"
               + createdBrowse_history.getBh_id();
   } 
   // 顯示編輯表單
   @GetMapping("/{bh_id}/edit")
   public String showEditForm(@PathVariable Integer bh_id, Model model) {
       return browseHistoryService.getBrowse_historyById(bh_id)
               .map(browse_history -> {
                   model.addAttribute("browse_history", browse_history);
                   model.addAttribute("isEdit", true);
                   return "browse_history/form";
               })
               .orElse("redirect:/web/browse_history");
   }
   
   // 處理編輯表單
   @PostMapping("/{bh_id}/edit")
   public String updateBrowse_history(@PathVariable Integer bh_id, 
   		//@PathVariable Integer product_id,
   		@ModelAttribute Browse_history browse_history,
   		RedirectAttributes redirectAttributes) {   	
   		Integer userId = (browse_history.getUsers() != null) ? 
   				browse_history.getUsers().getId() : null;
       if (userId == null) {
           throw new RuntimeException("編輯時必須指定使用者 ID");
       }     
       Integer productId = (browse_history.getProducts() != null) ? 
    		   browse_history.getProducts().getProduct_id() : null;
		if (productId == null) {
			throw new RuntimeException("編輯時必須指定商品 ID");
		}       
		browseHistoryService.updateBrowse_history(bh_id, userId, productId);
       redirectAttributes.addFlashAttribute("successMessage", "瀏覽紀錄更新成功！");
       return "redirect:/web/browse_history/" + bh_id;
   }
   
   // 刪除瀏覽紀錄
   @PostMapping("/{bh_id}/delete")
   public String deleteBrowse_history(@PathVariable Integer bh_id, 
   		RedirectAttributes redirectAttributes) {
       if (browseHistoryService.deleteBrowse_history(bh_id)) {
           redirectAttributes.addFlashAttribute("successMessage", "瀏覽紀錄刪除成功！");
       }
       return "redirect:/web/browse_history";
   }
}
