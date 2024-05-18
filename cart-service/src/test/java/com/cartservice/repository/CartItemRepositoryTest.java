package com.cartservice.repository;


import com.cartservice.models.Cart;
import com.cartservice.models.CartItem;
import com.cartservice.models.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@DataJpaTest
class CartItemRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Test
    void deleteByCart() {
        Cart cart = new Cart();
        entityManager.persist(cart);

        Product product = new Product();
        entityManager.persist(product);

        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        entityManager.persist(cartItem);

        cartItemRepository.deleteByCart(cart);


        List<CartItem> all = cartItemRepository.findAll();
        assertTrue(all.isEmpty());

    }
}