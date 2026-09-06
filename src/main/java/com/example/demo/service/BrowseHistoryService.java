package com.example.demo.service;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.model.Browse_history;
import com.example.demo.model.Cart_items;
import com.example.demo.model.Products;
import com.example.demo.model.Users;
import com.example.demo.repository.BrowseHistoryRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;

@Service
public class BrowseHistoryService {
	@Autowired
    private BrowseHistoryRepository browseHistoryRepository;

    @Autowired
    private UserRepository userRepository; 
    
    @Autowired
    private ProductRepository productRepository;
    
    public Browse_history createBrowse_history(
            Integer user_id,
            Integer product_id) {

        Users users = userRepository.findById(user_id)
                .orElseThrow(() ->
                    new RuntimeException("找不到對應的使用者 ID: " + user_id));

        Products products = productRepository.findById(product_id)
                .orElseThrow(() ->
                    new RuntimeException("找不到對應的商品 ID: " + product_id));

        Optional<Browse_history> existing =
                browseHistoryRepository
                    .findByUserIdAndProductId(user_id, product_id);
        			//.findByUsers_IdAndProducts_Product_id(user_id, product_id);

        if (existing.isPresent()) {

            // 已經看過 → 更新瀏覽時間
            Browse_history browse_history = existing.get();

            browse_history.setUsers(users);
            browse_history.setProducts(products);

            return browseHistoryRepository.save(browse_history);
        }

        // 第一次瀏覽 → 建立紀錄
        Browse_history browse_history = new Browse_history();

        browse_history.setUsers(users);
        browse_history.setProducts(products);

        return browseHistoryRepository.save(browse_history);
    }

    public Optional<Browse_history> getBrowse_historyById(Integer bh_id) {
        return browseHistoryRepository.findById(bh_id);
    }

    public List<Browse_history> getByUser_id(Integer user_id){
    	return browseHistoryRepository.findByUser_id(user_id);
    }
    public Integer getBrowse_historyCountByUser_id(Integer user_id){
    		return browseHistoryRepository.getBrowse_historyCountByUser_id(user_id);
    }
    public List<Browse_history> getAllBrowse_history() {
        return browseHistoryRepository.findAll();
    }

    public Browse_history updateBrowse_history(Integer bh_id, Integer user_id, 
    			Integer product_id) {
        
        Optional<Browse_history> existingBrowse_history = browseHistoryRepository.findById(bh_id);

        if (existingBrowse_history.isPresent()) {
        		Browse_history browse_history = existingBrowse_history.get();

            // 根據傳入的 order_id 尋找訂單物件
        		Users users = userRepository.findById(user_id)
                    .orElseThrow(() -> new RuntimeException("找不到對應的使用者 ID: " + user_id));
            Products products = productRepository.findById(product_id)
                    .orElseThrow(() -> new RuntimeException("找不到對應的商品 ID: " + product_id));
            browse_history.setUsers(users); 
            browse_history.setProducts(products);
           
            return browseHistoryRepository.save(browse_history);
        }

        throw new RuntimeException("瀏覽紀錄不存在: " + bh_id); 
    }

    public boolean deleteBrowse_history(Integer bh_id) {
        Optional<Browse_history> browse_history = browseHistoryRepository.findById(bh_id);
        if (browse_history.isPresent()) {
        		browseHistoryRepository.deleteById(bh_id);
            return true;
        }
        return false;
    }

    public long getBrowse_historyCount() {
        return browseHistoryRepository.count();
    }
}
