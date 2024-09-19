package com.example.identityService.service;

import com.example.identityService.dto.response.ProductImageResponse;
import com.example.identityService.entity.ProductImage;

import java.util.List;

public interface ProductImageService {
    List<ProductImage> getAllByproductId(String productId);
    ProductImage getMainImage(String productId);
    ProductImage getById(String productImageId);
    ProductImage update(ProductImage productImage);
    String delete(String productImageId);
}
