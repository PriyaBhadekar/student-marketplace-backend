package com.marketplace.studentmarketplace.service;

import com.marketplace.studentmarketplace.dto.request.ProductRequest;
import com.marketplace.studentmarketplace.dto.response.ProductResponse;
import com.marketplace.studentmarketplace.entity.User;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface ProductService {
    ProductResponse createProduct(User seller, ProductRequest request, MultipartFile file);
    List<ProductResponse> getAllAvailableProducts();
    List<ProductResponse> getMyProducts(User seller);
    ProductResponse getProductById(Long id);
    List<ProductResponse> searchProducts(String keyword);
    ProductResponse updateProduct(User seller, Long productId, ProductRequest request);
    void deleteProduct(User seller, Long productId);
}