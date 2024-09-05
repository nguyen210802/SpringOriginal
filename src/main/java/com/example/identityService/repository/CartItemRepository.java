package com.example.identityService.repository;

import com.example.identityService.entity.Cart;
import com.example.identityService.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, String> {
    List<CartItem> findAllByCart(Cart cart);
    void deleteAllByCart(Cart cart);
}
