package com.example.identityService.service.impl;

import com.example.identityService.entity.Cart;
import com.example.identityService.entity.CartItem;
import com.example.identityService.entity.Product;
import com.example.identityService.repository.CartItemRepository;
import com.example.identityService.repository.CartRepository;
import com.example.identityService.repository.ProductRepository;
import com.example.identityService.service.CartService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CartServiceImpl implements CartService {
    CartRepository cartRepository;
    CartItemRepository cartItemRepository;
    ProductRepository productRepository;


    @Override
    public Cart getMyCart() {
        var authenticated = SecurityContextHolder.getContext().getAuthentication();
        String userId = authenticated.getName();
        Cart cart = cartRepository.findByBuyer_Id(userId).orElseThrow();

        List<CartItem> cartItems = cartItemRepository.findAllByCartId(cart.getId());
        HashSet<CartItem> items = new HashSet<>(cartItems);

        cart.setCartItems(items);

        return cart;
    }

    @Override
    public Cart update(Cart cart) {
        return cartRepository.save(cart);
    }

    @Override
    public void delete(String userId) {
        Cart cart = cartRepository.findByBuyer_Id(userId).orElseThrow();
        cartRepository.delete(cart);
    }

    @Override
    @Transactional
    public Cart addProduct(String productId) {
        Cart cart = getMyCart();
        Product product = productRepository.findById(productId).orElseThrow();
        Set<CartItem> cartItems = cart.getCartItems();

        for (CartItem item : cartItems){
            if(item.getProduct().getId().equals(productId)){
                item.setQuantity(item.getQuantity() + 1);
                item.setPrice(item.getQuantity()*product.getPrice());
                cartItemRepository.save(item);
                return cart;
            }
        }

        CartItem cartItem = CartItem.builder()
                .cart(cart)
                .product(product)
                .quantity(1)
                .price(product.getPrice())
                .build();
        cartItemRepository.save(cartItem);
        return cart;
    }

    @Override
    @Transactional
    public Cart reduceProduct(String productId) {
        Cart cart = getMyCart();
        Product product = productRepository.findById(productId).orElseThrow();
        Iterator<CartItem> iterator = cart.getCartItems().iterator();

        while (iterator.hasNext()) {
            CartItem item = iterator.next();
            if (item.getProduct().getId().equals(productId)) {
                item.setQuantity(item.getQuantity() - 1);
                if (item.getQuantity() == 0) {
                    iterator.remove(); // Xóa an toàn từ Set
                    cartItemRepository.delete(item);
                } else {
                    item.setPrice(item.getQuantity()*product.getPrice());
                    cartItemRepository.save(item);
                }
                break;
            }
        }

        return cartRepository.save(cart); // Lưu cart sau khi cập nhật
    }
}
