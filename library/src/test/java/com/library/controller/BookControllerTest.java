package com.library.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.http.headers.HeadersGenerator;
import com.library.models.Book;
import com.library.models.BookResponse;
import com.library.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.client.HttpClientErrorException;


import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BookController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookServiceImpl bookService;

    @MockBean
    private HeadersGenerator headersGenerator;

    Book book;
    BookResponse bookResponse;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId(1);
        book.setTitle("Book Title");
        bookResponse = new BookResponse();
        bookResponse.setId(1);
        bookResponse.setTitle("Book Title");
    }

    @Test
    void addBookShouldReturnStatusIsCreate() throws Exception {
        when(bookService.productInDatabase(book.getId())).thenReturn(false);

        ResultActions response = mockMvc.perform(post("/v1/library/addBook")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(book))
        );

        response.andExpect(status().isCreated());
    }

    @Test
    void addBookShouldReturnStatusBadRequest() throws Exception {
        when(bookService.productInDatabase(book.getId())).thenReturn(true);

        ResultActions response = mockMvc.perform(post("/v1/library/addBook")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(book))
        );

        response.andExpect(status().isBadRequest());
    }

    @Test
    void getBooksShouldReturnStatusIsOk() throws Exception{
        List<Book> books = List.of(book);
        when(bookService.getAllBooks()).thenReturn(books);

        ResultActions response = mockMvc.perform(get("/v1/library/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(books))
        );

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$..id").value(1))
                .andExpect(jsonPath("$..title").value("Book Title"));


    }

    @Test
    void getBooksShouldReturnStatusBadRequest() throws Exception{
        List<Book> books = List.of();
        when(bookService.getAllBooks()).thenReturn(books);

        ResultActions response = mockMvc.perform(get("/v1/library/books")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(books)));

        response.andExpect(status().isBadRequest());
    }

    @Test
    void getBookByIdShouldReturnStatusIsOk() throws Exception {
        when(bookService.getBookById(1)).thenReturn(book);
        when(bookService.mapToBookResponse(book)).thenReturn(bookResponse);

        ResultActions response = mockMvc.perform(get("/v1/library/book/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookResponse))
        );

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Book Title"));


    }

    @Test
    void getBookByIdShouldReturnStatusNotFound() throws Exception {
        when(bookService.getBookById(1)).thenReturn(null);

        ResultActions response = mockMvc.perform(get("/v1/library/book/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookResponse))
        );

        response.andExpect(status().isNotFound());
    }

    @Test
    void deleteBookByIdShouldReturnStatusIsOk() throws Exception {
        when(bookService.getBookById(1)).thenReturn(book);

        ResultActions response = mockMvc.perform(delete("/v1/library/deleteBook/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(1))
        );

        response.andExpect(status().isOk());
    }

    @Test
    void deleteBookByIdShouldReturnStatusBadRequest() throws Exception {
        when(bookService.getBookById(1)).thenReturn(null);

        ResultActions response = mockMvc.perform(delete("/v1/library/deleteBook/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(1))
        );

        response.andExpect(status().isBadRequest());
    }
}