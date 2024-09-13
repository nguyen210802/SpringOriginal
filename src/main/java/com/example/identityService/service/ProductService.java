package com.example.identityService.service;

import com.example.identityService.dto.PageResponse;
import com.example.identityService.entity.Product;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    PageResponse<Product> getAll(int page, int size);
    Product getProductById(String id);
//    List<Product> getAll();
    PageResponse<Product> getAllMyProduct(int page, int size);
    Product create(Product product);
    Product update(String id, Product product);
    String delete(String id);
}
