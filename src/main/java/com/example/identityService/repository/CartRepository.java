package com.example.identityService.repository;

import com.example.identityService.entity.Cart;
import com.example.identityService.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, String> {
    Optional<Cart> findByUser_Id(String userId);
    void deleteByUser_Id(String userId);
}
