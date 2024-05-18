package com.library.service.impl;

import com.library.models.Book;
import com.library.models.BookResponse;
import com.library.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    private Book book;

    private BookResponse bookResponse;

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
    void getAllBooksShouldReturnListOfBooks() {
        List<Book> books = List.of(book);

        when(bookRepository.findAll()).thenReturn(books);

        List<Book> allBooks = bookService.getAllBooks();
        assertEquals(books, allBooks);
        assertEquals(book.getId(), allBooks.get(0).getId());
        assertEquals(book.getTitle(), allBooks.get(0).getTitle());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void getBookByIdShouldReturnBook() {
        int bookId = 1;
        when(bookRepository.findById(1)).thenReturn(Optional.of(book));

        Book bookById = bookService.getBookById(bookId);

        assertEquals(book, bookById);
        verify(bookRepository).findById(bookId);

    }

    @Test
    void mapToBookResponseShouldMapBookToBookResponse() {
        when(modelMapper.map(book, BookResponse.class)).thenReturn(bookResponse);

        BookResponse bookResponse1 = bookService.mapToBookResponse(book);

        assertEquals(bookResponse, bookResponse1);

    }

    @Test
    void deleteBookByIdShouldReturnSuccessMessage() {
        int bookId = 1;
        when(bookRepository.findById(1)).thenReturn(Optional.of(book));
        doNothing().when(bookRepository).deleteById(bookId);

        String info = bookService.deleteBookById(bookId);

        assertEquals(info,"Book successful deleted");
        verify(bookRepository).deleteById(bookId);
    }

    @Test
    void deleteBookByIdShouldReturnIllegalStateException() {
        when(bookRepository.findById(1)).thenReturn(null);

        assertThrows(NullPointerException.class, () -> bookService.deleteBookById(1));


    }


    @Test
    void productInDatabase() {

        int bookId = 1;
        when(bookRepository.findById(1)).thenReturn(Optional.of(book));

        Boolean b = bookService.productInDatabase(bookId);

        assertTrue(b);

    }
}