package com.example.identityService.service;

import com.example.identityService.entity.Product;

import java.util.List;

public interface ProductService {
    Product getProductById(String id);
    List<Product> getAll();
    Product create(Product product);
    Product update(String id, Product product);
    String delete(String id);
}
