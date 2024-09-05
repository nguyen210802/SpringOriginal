package com.example.identityService.service;

import com.example.identityService.entity.Cart;

public interface CartService{
    Cart getById(String id);
    Cart getByUserId(String userId);
    Cart create(Cart cart);
    Cart update(Cart cartUpdate);
    void delete(String userId);
}
