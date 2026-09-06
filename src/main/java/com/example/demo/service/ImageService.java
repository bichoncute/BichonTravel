package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Image;
import com.example.demo.model.Products;
import com.example.demo.repository.ImageRepository;
import com.example.demo.repository.ProductRepository;

@Service
public class ImageService {
	@Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ProductRepository productRepository; 

    public Image createImage(Integer product_id, 
    		String image_name, String image_url) {

    		Products products = productRepository.findById(product_id)
                .orElseThrow(() -> new RuntimeException("找不到對應的產品 ID: " + product_id));

        Image image = new Image();
        image.setProducts(products);
     
        //Image.setTotal_amount(total_amount);
        image.setImage_name(image_name);
        image.setImage_url(image_url);

        return imageRepository.save(image);
    }

    public Optional<Image> getImageById(Integer image_id) {
        return imageRepository.findById(image_id);
    }

    public List<Image> getAllImage() {
        return imageRepository.findAll();
    }

    public Image updateImage(Integer image_id, Integer product_id, 
    		String image_name, String image_url) {

        Optional<Image> existingImage = imageRepository.findById(image_id);

        if (existingImage.isPresent()) {
        		Image image = existingImage.get();
        		Products products = productRepository.findById(product_id)
                        .orElseThrow(() -> new RuntimeException("找不到對應的產品 ID: " + product_id));

        		//image.setImage_id(image_id);
        		image.setProducts(products);
        		//Image.setTotal_amount(total_amount);
        		image.setImage_name(image_name);
        		image.setImage_url(image_url);
            return imageRepository.save(image);
        }
        throw new RuntimeException("圖片不存在: " + image_id); 
    }

    public boolean deleteImage(Integer Image_id) {
        Optional<Image> image = imageRepository.findById(Image_id);
        if (image.isPresent()) {
        			imageRepository.deleteById(Image_id);
        			return true;
        }
        return false;
    }

    public long getImageCount() {
        return imageRepository.count();
    }
}
