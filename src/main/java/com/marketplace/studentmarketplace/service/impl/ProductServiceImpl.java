package com.marketplace.studentmarketplace.service.impl;

import com.marketplace.studentmarketplace.dto.request.ProductRequest;
import com.marketplace.studentmarketplace.dto.response.ProductResponse;
import com.marketplace.studentmarketplace.entity.*;
import com.marketplace.studentmarketplace.enums.*;
import com.marketplace.studentmarketplace.exception.*;
import com.marketplace.studentmarketplace.repository.ProductRepository;
import com.marketplace.studentmarketplace.service.ProductService;
import com.marketplace.studentmarketplace.util.FileStorageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final FileStorageUtil fileStorageUtil;

    @Value("${server.port:8080}")
    private String serverPort;

    @Override
    public ProductResponse createProduct(User seller, ProductRequest request, MultipartFile file) {
        if (request.getProductType() == ProductType.DIGITAL && (file == null || file.isEmpty())) {
            throw new BadRequestException("PDF file is required for digital products");
        }

        String filePath = null;
        if (file != null && !file.isEmpty()) {
            filePath = fileStorageUtil.saveFile(file);
        }

        Product product = Product.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .price(request.getPrice())
                .productType(request.getProductType())
                .subject(request.getSubject())
                .semester(request.getSemester())
                .filePath(filePath)
                .quantity(request.getQuantity() != null ? request.getQuantity() : 1) // ✅
                .seller(seller)
                .build();

        return mapToResponse(productRepository.save(product));
    }

    @Override
    public List<ProductResponse> getAllAvailableProducts() {
        // ✅ Only returns AVAILABLE products (quantity > 0 enforced via status)
        return productRepository.findByStatus(ProductStatus.AVAILABLE)
                .stream().map(this::mapToResponse).toList();
    }

    @Override
    public List<ProductResponse> getMyProducts(User seller) {
        return productRepository.findBySeller(seller)
                .stream().map(this::mapToResponse).toList();
    }

    @Override
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return mapToResponse(product);
    }

    @Override
    public List<ProductResponse> searchProducts(String keyword) {
        return productRepository
                .findByTitleContainingIgnoreCaseOrSubjectContainingIgnoreCase(keyword, keyword)
                .stream().map(this::mapToResponse).toList();
    }

    @Override
    public ProductResponse updateProduct(User seller, Long productId, ProductRequest request) {
        Product product = getProductOwnedBySeller(seller, productId);
        product.setTitle(request.getTitle());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setSubject(request.getSubject());
        product.setSemester(request.getSemester());
        if (request.getQuantity() != null) {
            product.setQuantity(request.getQuantity());
            // Re-open if seller restocks
            if (request.getQuantity() > 0 && product.getStatus() == ProductStatus.SOLD_OUT) {
                product.setStatus(ProductStatus.AVAILABLE);
            }
        }
        return mapToResponse(productRepository.save(product));
    }

    @Override
    public void deleteProduct(User seller, Long productId) {
        Product product = getProductOwnedBySeller(seller, productId);
        productRepository.delete(product);
    }

    private Product getProductOwnedBySeller(User seller, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        if (!product.getSeller().getId().equals(seller.getId())) {
            throw new BadRequestException("You don't have permission to modify this product");
        }
        return product;
    }

    // ✅ Package-private so OrderServiceImpl can use it
    public ProductResponse mapToResponse(Product product) {
        String fileUrl = product.getFilePath() != null
                ? "http://localhost:" + serverPort + "/api/files/" + product.getFilePath()
                : null;
        return ProductResponse.builder()
                .id(product.getId())
                .title(product.getTitle())
                .description(product.getDescription())
                .price(product.getPrice())
                .productType(product.getProductType())
                .status(product.getStatus())
                .subject(product.getSubject())
                .semester(product.getSemester())
                .fileUrl(fileUrl)
                .quantity(product.getQuantity())   // ✅ NEW
                .sellerName(product.getSeller().getFullName())
                .sellerCollege(product.getSeller().getCollegeName())
                .createdAt(product.getCreatedAt())
                .build();
    }
}