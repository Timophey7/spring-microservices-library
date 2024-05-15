package com.cartservice.service;


import com.cartservice.exceptions.ProductNotFoundException;
import com.cartservice.models.BookResponse;
import com.cartservice.models.Product;
import jakarta.transaction.Transactional;

import java.util.List;

public interface ProductService {
    BookResponse getBookResponse(int id);

    Product getProduct(int id) throws ProductNotFoundException;

    List<Integer> getProductsIds(Long id);

    @Transactional
    void saveProduct(Product product);

    @Transactional
    Product saveProduct(int id);

    Product mapToProduct(BookResponse productResponse);

    Boolean productNotExists(int id);

}
