package com.cartservice.service.impl;


import com.cartservice.exceptions.ProductNotFoundException;
import com.cartservice.models.BookResponse;
import com.cartservice.models.Product;
import com.cartservice.repository.ProductRepository;
import com.cartservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final WebClient.Builder webClientBuilder;

    @Override
    public BookResponse getBookResponse(int id){
        BookResponse productResponse = webClientBuilder
                .build()
                .get()
                .uri("http://electronic-library/v1/library/book/" + id)
                .retrieve()
                .bodyToMono(BookResponse.class).block();
        log.info("get product by id from library: {}", productResponse);
        return productResponse;
    }

    @Override
    public Product getProduct(int id) throws ProductNotFoundException {
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("not found"));
        return product;
    }

    @Override
    public List<Integer> getProductsIds(Long id) {
        return productRepository.getProductsIds(id);
    }


    @Override
    public void saveProduct(Product product){
        productRepository.save(product);
    }

    @Override
    public Product saveProduct(int id) {
        BookResponse productResponce = getBookResponse(id);
        log.info("Saving product " + productResponce.getId());
        Product product = mapToProduct(productResponce);
        log.info("map product " + product.getId());
        saveProduct(product);
        return product;
    }


    @Override
    public Product mapToProduct(BookResponse productResponse){
        Product product = new Product();
        product.setId(productResponse.getId());
        product.setTitle(productResponse.getTitle());
        product.setPrice(productResponse.getPrice());
        return product;
    }

    @Override
    public Boolean productNotExists(int id){
        BookResponse productResponse = getBookResponse(id);
        return productResponse == null;
    }

    public boolean productInDB(int id){
        return productRepository.existsById(id);
    }





}
