package com.example.identityService.service;

import com.example.identityService.dto.response.ProductImageResponse;
import com.example.identityService.entity.ProductImage;

import java.util.List;

public interface ProductImageService {
    List<ProductImage> getAllByproductId(String productId);
    ProductImage getMainImage(String productId);
    ProductImageResponse update(ProductImage product);
    String delete(String productImageId);
}
