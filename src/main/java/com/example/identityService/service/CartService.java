package com.example.identityService.service;

import com.example.identityService.entity.Cart;

public interface CartService{
    Cart getMyCart();
    int countMyCart();
    Cart update(Cart cartUpdate);
    void delete(String userId);

    Cart addProduct(String productId);
    Cart reduceProduct(String productId);
    Cart deleteProduct(String cartItemId);
}
