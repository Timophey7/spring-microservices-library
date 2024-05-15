package com.library.service;

import com.library.models.Book;
import com.library.models.BookResponse;
import jakarta.transaction.Transactional;

import java.util.List;

public interface BookService {
    List<Book> getAllBooks();

    @Transactional
    Book getBookById(int id);

    BookResponse mapToBookResponse(Book book);

    @Transactional
    String deleteBookById(int id);

    @Transactional
    String saveBook(Book book);

    Boolean productInDatabase(int id);
}
