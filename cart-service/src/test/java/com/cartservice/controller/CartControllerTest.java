package com.cartservice.controller;

import com.cartservice.exceptions.CartServiceException;
import com.cartservice.exceptions.ProductNotFoundException;
import com.cartservice.headers.HeadersGenerator;
import com.cartservice.models.Cart;
import com.cartservice.models.CartDto;
import com.cartservice.models.CartResponse;
import com.cartservice.models.Product;
import com.cartservice.service.impl.CartServiceImpl;
import com.cartservice.service.impl.EmailServiceImpl;
import com.cartservice.service.impl.KafkaDataServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CartController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    EmailServiceImpl emailService;

    @MockBean
    ApplicationEventPublisher eventPublisher;

    @MockBean
    KafkaDataServiceImpl kafkaDataService;

    @MockBean
    HeadersGenerator headersGenerator;

    @MockBean
    CartServiceImpl cartService;
    Cart cart;
    CartDto cartDto;
    Product product;
    CartResponse cartResponse;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1);
        cart = new Cart();
        cart.setId(1L);
        cartDto = new CartDto();
        cartDto.setId(1L);
        cartDto.setProductIds(List.of(1,2));
        cartResponse = new CartResponse();
        cartResponse.setCartId(1L);
        cartResponse.setResultSum(100);
    }

    @Test
    void addProductInCartShouldReturnCreatedStatus() throws Exception {
        willDoNothing().given(cartService).saveProductInCart(any(CartDto.class),anyInt());


        ResultActions response = mockMvc.perform(post("/v1/user/libraryCart/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(cartDto)));

        response.andExpect(status().isCreated());

    }

    @Test
    void addProductInCartShouldReturnNotFoundStatus() throws Exception {
        willThrow(ProductNotFoundException.class).given(cartService).saveProductInCart(any(CartDto.class),anyInt());

        ResultActions response = mockMvc.perform(post("/v1/user/libraryCart/cart")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(cartDto)));

        response.andExpect(status().isNotFound());
    }

    @Test
    void getAllProductFromCartShouldReturnOkStatus() throws Exception {
        Long id = 1L;
        when(cartService.getCartById(id)).thenReturn(cart);
        when(cartService.getProductFromCart(id)).thenReturn(List.of(product));

        ResultActions response = mockMvc.perform(get("/v1/user/libraryCart/cart/" + cart.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(List.of(product)))
        );

        response.andExpect(status().isOk());

    }

    @Test
    void getProductFromCartShouldReturnNotFoundStatus() throws Exception {
        Long id = 1L;
        when(cartService.getCartById(id)).thenReturn(null);

        when(cartService.getProductFromCart(id)).thenReturn(List.of(product));

        ResultActions response = mockMvc.perform(get("/v1/user/libraryCart/cart/" + cart.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(List.of(product)))
        );

        response.andExpect(status().isNotFound());
    }

    @Test
    void getProductFromCartShouldReturnProductNotFoundException() throws Exception {
        Long id = 1L;
        when(cartService.getCartById(id)).thenReturn(cart);
        when(cartService.getProductFromCart(id)).thenThrow(ProductNotFoundException.class);

        ResultActions response = mockMvc.perform(get("/v1/user/libraryCart/cart/" + cart.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(List.of(product)))
        );

        response.andExpect(status().isNotFound());
    }

    @Test
    void getProductFromCartShouldReturnCartServiceException() throws Exception {
        Long id = 1L;
        when(cartService.getCartById(id)).thenReturn(cart);
        when(cartService.getProductFromCart(id)).thenThrow(CartServiceException.class);

        ResultActions response = mockMvc.perform(get("/v1/user/libraryCart/cart/" + cart.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(List.of(product)))
        );

        response.andExpect(status().isNotFound());
    }


    @Test
    void cleanCartShouldReturnStatusIsOk() throws Exception {
        Long id = 1L;
        when(cartService.getCartById(id)).thenReturn(cart);
        doNothing().when(cartService).cleanCart(id);

        ResultActions response = mockMvc.perform(delete("/v1/user/libraryCart/cart/" + cart.getId())
                .contentType(MediaType.APPLICATION_JSON
                )
        );

        response.andExpect(status().isOk());
        verify(cartService).cleanCart(id);

    }

    @Test
    void cleanCartShouldReturnStatusNotFound() throws Exception {
        Long id = 1L;
        when(cartService.getCartById(id)).thenReturn(null);
        doNothing().when(cartService).cleanCart(id);

        ResultActions response = mockMvc.perform(delete("/v1/user/libraryCart/cart/" + cart.getId())
                .contentType(MediaType.APPLICATION_JSON
                )
        );

        response.andExpect(status().isNotFound());
        verify(cartService,never()).cleanCart(id);
    }


    @Test
    void createOrderShouldReturnStatusCreatedStatus() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Long id = 1L;
        String email = "test@gmail.com";
        int resultPrice = 100;
        when(cartService.getCartById(id)).thenReturn(cart);
        when(cartService.getResultSum(id)).thenReturn(resultPrice);
        when(emailService.getEmailFromToken(request)).thenReturn(email);

        ResultActions response = mockMvc.perform(post("/v1/user/libraryCart//createOrder/" + cart.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(cartResponse))

        );

        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.cartId").value(id))
                .andExpect(jsonPath("$.resultSum").value(resultPrice));


    }

    @Test
    void createOrderShouldReturnNotFoundStatus() throws Exception {
        Long id = 1L;
        when(cartService.getCartById(id)).thenReturn(null);

        ResultActions response = mockMvc.perform(post("/v1/user/libraryCart//createOrder/" + cart.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(cartResponse))

        );

        response.andExpect(status().isNotFound());
    }
}