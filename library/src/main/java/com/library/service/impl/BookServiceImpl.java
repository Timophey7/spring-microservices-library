package com.library.service.impl;

import com.library.models.Book;
import com.library.models.BookResponse;
import com.library.repository.BookRepository;
import com.library.service.BookService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final ModelMapper modelMapper;

    private final BookRepository bookRepository;

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
//    @Cacheable(value = "redis", key = "#id")
    public Book getBookById(int id) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        Book book = optionalBook.orElse(null);
        return book;
    }

    @Override
    public BookResponse mapToBookResponse(Book book) {
        return modelMapper.map(book, BookResponse.class);
    }

    @Override
    public String deleteBookById(int id) {
        if (getBookById(id) == null) {
            throw new NullPointerException();
        }
        bookRepository.deleteById(id);
        return "Book successful deleted";
    }

    @Override
    public String saveBook(Book book) {
        bookRepository.save(book);
        return "Book successful saved";
    }

    @Override
    public Boolean productInDatabase(int id) {
        Book book = bookRepository.findById(id).orElse(null);
        return book != null;
    }

}
