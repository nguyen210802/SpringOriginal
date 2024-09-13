package com.example.identityService.repository;

import com.example.identityService.entity.Cart;
import com.example.identityService.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, String> {
    Optional<Cart> findByBuyer_Id(String userId);
}
