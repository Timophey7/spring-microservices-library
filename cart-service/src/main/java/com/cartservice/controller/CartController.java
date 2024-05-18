package com.cartservice.controller;


import com.cartservice.events.OrderCreateEvent;
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
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/v1/user/libraryCart")
@RequiredArgsConstructor
@Slf4j
public class CartController {

    private final EmailServiceImpl emailServiceImpl;

    private final ApplicationEventPublisher eventPublisher;

    private final KafkaDataServiceImpl kafkaDataService;

    private final HeadersGenerator headersGenerator;

    private final CartServiceImpl cartService;

    @PostMapping("/cart")
    public ResponseEntity<?> addProductInCart(
            @RequestBody CartDto cartDto
    ) {
        for (int id : cartDto.getProductIds()) {
            try {
                cartService.saveProductInCart(cartDto,id);
            } catch (ProductNotFoundException e) {
                e.printStackTrace();
                return new ResponseEntity<>(
                        "product not found:"+id,
                        headersGenerator.getHeadersForError(),
                        HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<>(
                headersGenerator.getHeadersForSuccessPostMethod(),
                HttpStatus.CREATED
        );

    }

    @GetMapping("/cart/{id}")
    public ResponseEntity<?> getAllProductFromCart(@PathVariable("id") Long id){
        if (cartService.getCartById(id) != null){
            try {
                List<Product> productFromCart = cartService.getProductFromCart(id);
                return new ResponseEntity<List<Product>>(
                        productFromCart,
                        headersGenerator.getHeaderForSuccessGetMethod(),
                        HttpStatus.OK
                );
            }catch (ProductNotFoundException e){
                e.printStackTrace();
                log.error(e.getMessage());
                return new ResponseEntity<>(
                        "product not found",
                        headersGenerator.getHeadersForError(),
                        HttpStatus.NOT_FOUND
                );
            } catch (CartServiceException e) {
                e.printStackTrace();
                return new ResponseEntity<>(
                        "cart not found",
                        headersGenerator.getHeadersForError(),
                        HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<>(
                "cart not found",
                headersGenerator.getHeadersForError(),
                HttpStatus.NOT_FOUND
        );
    }

    @DeleteMapping("/cart/{id}")
    public ResponseEntity<String> cleanCart(@PathVariable("id") Long id){
        if (cartService.getCartById(id) != null){
            try{
                cartService.cleanCart(id);
                return new ResponseEntity<String>(
                        "Карзина пуста",
                        headersGenerator.getHeaderForSuccessGetMethod(),
                        HttpStatus.OK
                );
            }catch (Exception e){
                e.printStackTrace();
                log.error(e.getMessage());
                return new ResponseEntity<String>(
                        "Возникла ошибка",
                        headersGenerator.getHeadersForError(),
                        HttpStatus.BAD_REQUEST
                );
            }
        }
        return new ResponseEntity<>(
                headersGenerator.getHeadersForError(),
                HttpStatus.NOT_FOUND
        );
    }

    @PostMapping("/createOrder/{cartId}")
    public ResponseEntity<?> createOrder(@PathVariable("cartId") Long cartId, HttpServletRequest request) {
        Cart cart = cartService.getCartById(cartId);
        if (cart == null) {
            return new ResponseEntity<String>(
                    "Корзина не найдена",
                    headersGenerator.getHeadersForError(),
                    HttpStatus.NOT_FOUND
            );
        }
        Integer resultSum = cartService.getResultSum(cartId);
        CartResponse cartResponse = new CartResponse();
        cartResponse.setCartId(cart.getId());
        cartResponse.setResultSum(resultSum);
        try{
            String email = emailServiceImpl.getEmailFromToken(request);
            kafkaDataService.send(cartResponse);
            eventPublisher.publishEvent(new OrderCreateEvent(cartId,resultSum,email));
            return new ResponseEntity<>(
                    cartResponse,
                    headersGenerator.getHeadersForSuccessPostMethod(),
                    HttpStatus.CREATED
            );
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
            return new ResponseEntity<>(
                    headersGenerator.getHeadersForError(),
                    HttpStatus.BAD_REQUEST
            );
        }

    }




}
