package com.cartservice.service.impl;

import com.cartservice.exceptions.CartServiceException;
import com.cartservice.exceptions.ProductNotFoundException;
import com.cartservice.models.Cart;
import com.cartservice.models.CartDto;
import com.cartservice.models.CartItem;
import com.cartservice.models.Product;
import com.cartservice.repository.CartItemRepository;
import com.cartservice.repository.CartRepository;
import com.cartservice.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    ProductServiceImpl productService;

    @Mock
    CartRepository cartRepository;

    @Mock
    CartItemRepository cartItemRepository;

    @Mock
    CartService cartService;

    @InjectMocks
    CartServiceImpl cartServiceImpl;

    @BeforeEach
    void setUp() {

        cartRepository = Mockito.mock(CartRepository.class);
        productService = Mockito.mock(ProductServiceImpl.class);
        cartItemRepository = Mockito.mock(CartItemRepository.class);
        cartServiceImpl = new CartServiceImpl(productService,cartRepository,cartItemRepository);
    }

    @Test
    void getCartByIdShouldReturnCart() {
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setItems(List.of(new CartItem()));
        cart.setDateOfCreate(new Date());

        cartRepository.save(cart);

        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));

        Cart cartById = cartServiceImpl.getCartById(1L);

        assertNotNull(cartById);
        assertEquals(cartById,cart);
        verify(cartRepository).findById(1L);

    }

    @Test
    void getProductFromCartShouldReturnAllProducts() throws ProductNotFoundException, CartServiceException {

        Long cartId = 1L;
        List<Integer> productIds = List.of(1,2,3);
        List<Product> expectedProducts = new ArrayList<>();

        for (Integer productId : productIds) {
            Product product = new Product();
            product.setId(productId);
            product.setTitle("Book"+productId);
            expectedProducts.add(product);
        }

        when(cartRepository.existsById(cartId)).thenReturn(true);

        when(productService.getProductsIds(cartId)).thenReturn(productIds);

        for (Integer productId : productIds) {
            when(productService.getProduct(productId)).thenAnswer((invocation) ->{
                Object id = invocation.getArgument(0);
                return expectedProducts
                            .stream()
                            .filter(p -> id.equals(p.getId()))
                            .findFirst()
                            .orElse(null);
            });


        }

        List<Product> productFromCart = cartServiceImpl.getProductFromCart(cartId);

        verify(productService).getProductsIds(cartId);

        assertEquals(productFromCart,expectedProducts);


    }

    @Test
    void getProductFromCartShouldReturnCartServiceException() throws ProductNotFoundException, CartServiceException {
        Long cartId = 1L;

        when(cartRepository.existsById(cartId)).thenReturn(false);

        assertThrows(CartServiceException.class, () -> cartServiceImpl.getProductFromCart(cartId));
    }

    @Test
    void saveProductInCartShouldCreateCartAndAddProduct() throws ProductNotFoundException {

        CartDto cartDto = new CartDto();
        cartDto.setId(1L);
        int productId = 1;

        when(productService.productInDB(productId)).thenReturn(false);

        cartServiceImpl.saveProductInCart(cartDto, productId);

        verify(productService).saveProduct(productId);
        verify(cartRepository).save(Mockito.any(Cart.class));
    }

    @Test
    void saveProductInCartWithoutCreateNewCart() throws ProductNotFoundException {
        CartDto cartDto = new CartDto();
        cartDto.setId(1L);
        int productId = 1;

        when(productService.productInDB(productId)).thenReturn(false);

        Cart cart = new Cart();
        when(cartRepository.findById(cartDto.getId())).thenReturn(Optional.of(cart));

        cartServiceImpl.saveProductInCart(cartDto, productId);

        verify(productService).saveProduct(productId);
        verify(cartRepository).save(Mockito.any(Cart.class));

    }

    @Test
    void saveProductInCartWhereCartAndProductInDB() throws ProductNotFoundException {
        CartDto cartDto = new CartDto();
        cartDto.setId(1L);
        int productId = 1;

        Cart cart = new Cart();
        cart.setId(1L);

        when(cartRepository.findById(cartDto.getId())).thenReturn(Optional.of(cart));
        when(productService.productInDB(productId)).thenReturn(true);
        cartServiceImpl.saveProductInCart(cartDto, productId);

        verify(cartRepository).save(Mockito.any(Cart.class));
        verify(productService).saveProduct(productId);
    }

    @Test
    void cleanCart() {

        Long id = 1L;
        Cart cart = new Cart();
        cart.setId(id);

        when(cartRepository.findById(id)).thenReturn(Optional.of(cart));
        cartServiceImpl.cleanCart(id);

        verify(cartItemRepository).deleteByCart(cart);
    }

    @Test
    void getResultSumWithoutException() throws ProductNotFoundException, CartServiceException {
        long cartId = 1L;
        Product product1 = new Product();
        product1.setPrice(100);
        Product product2 = new Product();
        product2.setPrice(200);
        List<Product> products = Arrays.asList(product1, product2);

        when(cartRepository.existsById(cartId)).thenReturn(true);
        when(productService.getProductsIds(cartId)).thenReturn(Arrays.asList(1, 2));
        when(productService.getProduct(1)).thenReturn(product1);
        when(productService.getProduct(2)).thenReturn(product2);

        Integer resultSum = cartServiceImpl.getResultSum(cartId);

        assertEquals(300, resultSum);
    }

    @Test
    void  getResultSumWithProductNotFoundExceptionAndCartNotFound() throws ProductNotFoundException, CartServiceException {

        Long cartId = 1L;

        Integer resultSum = cartServiceImpl.getResultSum(cartId);

        assertEquals(0, resultSum);

    }

    @Test
    void saveCart() throws ProductNotFoundException {
        Cart cart = new Cart();
        int productId = 1;
        CartItem cartItem = new CartItem();

        Product product = new Product();
        product.setId(productId);

        when(productService.saveProduct(productId)).thenReturn(product);

        cartServiceImpl.saveCart(cart, productId, cartItem);

        verify(productService).saveProduct(productId);
        verify(cartRepository).save(cart);
    }
}