package com.ecommerce.project.service;

import com.ecommerce.project.payload.DTO.CartDTO;
import jakarta.transaction.Transactional;

import java.util.List;

public interface CartService {
    CartDTO addProductToCart(Long productId, Integer quantity);

    List<CartDTO> getAllCarts();

    @Transactional
    CartDTO updateProductQuantityInCart(Long productId, Integer quantity);

    CartDTO getCart(String emailId, Long cartId);

    String deleteProductFromCart(Long cartId, Long productId);

    void updateProductInCarts(Long cartId, Long productId);
}