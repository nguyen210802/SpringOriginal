package com.example.identityService.repository;

import com.example.identityService.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, String> {
    List<CartItem> findAllByCartId(String cartId);
    void deleteAllByProduct_Id(String productId);
    List<CartItem> findByProductNameContainingIgnoreCase(String productName);
}
