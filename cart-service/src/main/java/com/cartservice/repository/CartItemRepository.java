package com.cartservice.repository;

import com.cartservice.models.Cart;
import com.cartservice.models.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    void deleteByCart(Cart cart);

}
