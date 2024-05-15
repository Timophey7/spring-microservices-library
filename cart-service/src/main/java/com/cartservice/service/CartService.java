package com.cartservice.service;



import com.cartservice.exceptions.CartServiceException;
import com.cartservice.exceptions.ProductNotFoundException;
import com.cartservice.models.Cart;
import com.cartservice.models.CartDto;
import com.cartservice.models.CartItem;
import com.cartservice.models.Product;
import jakarta.transaction.Transactional;

import java.util.List;

public interface CartService {

    Cart getCartById(Long cartId) ;

    List<Product> getProductFromCart(Long cartId) throws ProductNotFoundException, CartServiceException;

    @Transactional
    void saveProductInCart(CartDto cartDto, int id) throws ProductNotFoundException;

    void cleanCart(Long id);

    Integer getResultSum(long cartId);

    @Transactional
    void saveCart(Cart cart, int productId, CartItem cartItem) throws ProductNotFoundException;

}
