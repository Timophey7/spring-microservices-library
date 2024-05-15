package com.cartservice.repository;


import com.cartservice.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query(value = "SELECT product_id FROM `cart-service`.cart_items where cart_id = ?1", nativeQuery = true)
    List<Integer> getProductsIds(Long cartId);


}
