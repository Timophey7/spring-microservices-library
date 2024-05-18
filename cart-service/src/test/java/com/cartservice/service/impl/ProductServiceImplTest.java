package com.cartservice.service.impl;

import com.cartservice.exceptions.ProductNotFoundException;
import com.cartservice.models.BookResponse;
import com.cartservice.models.Product;
import com.cartservice.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;



@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private WebClient.Builder webClientBuilder;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        productRepository = Mockito.mock(ProductRepository.class);
        webClientBuilder = Mockito.mock(WebClient.Builder.class);
        productService = new ProductServiceImpl(productRepository, webClientBuilder);
    }

//    @Test
//    @Disabled
//    void getBookResponse() {
//        int productId = 1;
//        BookResponse bookResponse = new BookResponse();
//        bookResponse.setId(productId);
//        WebClient.Builder webClientBuilder = Mockito.mock(WebClient.Builder.class);
//        WebClient webClient = webClientBuilder.build();
//        BookResponse bookResponse1 = webClient
//                .get()
//                .uri("http://electronic-library/v1/library/book/" + productId)
//                .retrieve()
//                .bodyToMono(BookResponse.class)
//                .block();
//
//        when(webClientBuilder.build()).thenReturn(webClient);
//        when(webClient
//                .get()
//                .uri("http://electronic-library/v1/library/book/" + productId)
//                .retrieve()
//                .bodyToMono(BookResponse.class)
//                .block()
//        ).thenReturn(bookResponse);
//
//        BookResponse response = productService.getBookResponse(productId);
//        assertEquals(response, bookResponse);
//
//
//    }

    @Test
    void getProductShouldReturnProduct() throws ProductNotFoundException {
        int productId = 1;
        Product product = new Product();
        product.setId(productId);

        productRepository.save(product);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        Product serviceProduct = productService.getProduct(productId);

        assertEquals(product,serviceProduct);
    }

    @Test
    void getProductShouldReturnProductNotFoundException() throws ProductNotFoundException {
        int productId = 1;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.getProduct(productId));
    }

    @Test
    void getProductsIds() {
        Long cartId = 1L;
        List<Integer> productsIds = List.of(1, 2, 3);

        when(productRepository.getProductsIds(cartId)).thenReturn(productsIds);

        List<Integer> ids = productService.getProductsIds(cartId);

        assertEquals(productsIds,ids);

    }

//    @Test
//    @Disabled
//    void saveProduct() {
//        int productId = 1;
//        BookResponse bookResponse = new BookResponse();
//        bookResponse.setId(productId);
//        Product product = new Product();
//        product.setId(productId);
//
//        when(webClientBuilder
//                .build()
//                .get()
//                .uri("http://electronic-library/v1/library/book/" + productId)
//                .retrieve()
//                .bodyToMono(BookResponse.class)
//                .block()
//        ).thenReturn(bookResponse);
//        when(productService.getBookResponse(productId)).thenReturn(bookResponse);
//        when(productService.mapToProduct(bookResponse)).thenReturn(product);
//        productService.saveProduct(product);
//
//        Product saveProduct = productService.saveProduct(productId);
//
//        assertEquals(product,saveProduct);
//
//
//    }

    @Test
    void testSaveProduct() {
        Product product = new Product();
        product.setId(1);

        when(productRepository.save(product)).thenReturn(product);

        productService.saveProduct(product);

        verify(productRepository).save(product);

    }

    @Test
    void mapToProduct() {
        BookResponse bookResponse = new BookResponse();
        bookResponse.setId(1);
        bookResponse.setTitle("title");
        bookResponse.setPrice(100);

        Product product = productService.mapToProduct(bookResponse);

        assertEquals(bookResponse.getId(),product.getId());
        assertEquals(bookResponse.getTitle(),product.getTitle());
        assertEquals(bookResponse.getPrice(),product.getPrice());
    }

    @Test
    void productNotExists() {
    }

    @Test
    void productInDB() {
        int productId = 1;

        when(productRepository.existsById(productId)).thenReturn(true);

        boolean productInDB = productService.productInDB(productId);

        assertTrue(productInDB);


    }
}