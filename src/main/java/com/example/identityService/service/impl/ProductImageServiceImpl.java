package com.example.identityService.service.impl;

import com.example.identityService.dto.response.ProductImageResponse;
import com.example.identityService.entity.ProductImage;
import com.example.identityService.repository.ProductImageRepository;
import com.example.identityService.service.ProductImageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProductImageServiceImpl implements ProductImageService {
    ProductImageRepository productImageRepository;

    @Override
    public List<ProductImage> getAllByproductId(String productId) {
        return productImageRepository.findAllByProduct_Id(productId);
    }

    @Override
    public ProductImage getMainImage(String productId) {
        return productImageRepository.findMainImageByProduct_Id(productId);
    }

    @Override
    public ProductImageResponse update(ProductImage product) {
        return null;
    }

    @Override
    public String delete(String productImageId) {
        productImageRepository.deleteById(productImageId);
        return "Delete successfully";
    }
}
