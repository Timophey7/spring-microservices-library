package com.cartservice.service.impl;

import com.cartservice.exceptions.CartServiceException;
import com.cartservice.exceptions.ProductNotFoundException;
import com.cartservice.models.*;
import com.cartservice.repository.CartItemRepository;
import com.cartservice.repository.CartRepository;
import com.cartservice.service.CartService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private static final Logger log = LoggerFactory.getLogger(CartServiceImpl.class);
    private final ProductServiceImpl productService;

    private final CartRepository cartRepository;

    private final CartItemRepository cartItemRepository;


    @Override
    public Cart getCartById(Long cartId)   {
        Cart cart = cartRepository.findById(cartId).orElse(null);
        return cart;
    }

    @Override
    public List<Product> getProductFromCart(Long cartId) throws ProductNotFoundException, CartServiceException {
        if (!cartRepository.existsById(cartId)) {
            throw new CartServiceException("Cart not found");
        }
        List<Product> products = new ArrayList<>();
        List<Integer> productsIds = productService.getProductsIds(cartId);
        for (Integer productId : productsIds) {
            Product product = productService.getProduct(productId);
            products.add(product);
        }
        return products;
    }

    @Override
    public void saveProductInCart(CartDto cartDto, int id) throws ProductNotFoundException {
        CartItem cartItem = new CartItem();
        if (getCartById(cartDto.getId()) == null) {
            Cart cart = new Cart();
            cart.setId(cartDto.getId());
            cart.setDateOfCreate(new Date());
            if (!productService.productInDB(id)){
                Product product = productService.saveProduct(id);
                log.info("Saving product in cart: "+ product);
                cartItem.setProduct(product);
                cart.addItem(cartItem);
                cartRepository.save(cart);
            }else {
                saveCart(cart,id,cartItem);
            }
        }else {
            Cart cart = getCartById(cartDto.getId());
            if (!productService.productInDB(id)){
                Product product = productService.saveProduct(id);
                cartItem.setProduct(product);
                cart.addItem(cartItem);
                cartRepository.save(cart);
            }else {
                saveCart(cart,id,cartItem);
            }
        }
    }


    @Override
    @Transactional
    public void cleanCart(Long id) {
        Cart cart = getCartById(id);
        cartItemRepository.deleteByCart(cart);
    }

    @Override
    public Integer getResultSum(long cartId) {
        try {
            List<Product> productsFromCart = getProductFromCart(cartId);
            Integer sum = 0;
            for(Product product : productsFromCart){
                sum += product.getPrice();
            }
            return sum;
        }catch (ProductNotFoundException e){
            e.printStackTrace();
            return 0;
        } catch (CartServiceException e) {
            e.printStackTrace();
            return 0;
        }

    }

    @Override
    public void saveCart(Cart cart, int productId, CartItem cartItem) throws ProductNotFoundException {
        Product product = productService.saveProduct(productId);
        cartItem.setProduct(product);
        cart.addItem(cartItem);
        cartRepository.save(cart);
    }


}
