package com.library.controller;

import com.library.http.headers.HeadersGenerator;
import com.library.models.Book;
import com.library.models.BookResponse;
import com.library.service.impl.BookServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/v1/library")
@RequiredArgsConstructor
@Slf4j
public class BookController {

    private final BookServiceImpl bookService;

    private final HeadersGenerator headersGenerator;


    @PostMapping(value = "/addBook")
    public ResponseEntity<Book> addBook(
            @RequestBody Book book

    ) {
        if (!bookService.productInDatabase(book.getId())) {
            try {
                bookService.saveBook(book);
                return new ResponseEntity<Book>(
                        headersGenerator.getHeadersForSuccessPostMethod(),
                        HttpStatus.CREATED
                );
            }catch (Exception e) {
                log.error(e.getMessage());
                return new ResponseEntity<Book>(
                        headersGenerator.getHeadersForError(),
                        HttpStatus.BAD_REQUEST
                );
            }
        }

        return new ResponseEntity<Book>(
                headersGenerator.getHeadersForError(),
                HttpStatus.BAD_REQUEST
        );

    }

    @GetMapping(value = "/books")
    public ResponseEntity<List<Book>> getBooks(){
        List<Book> allBooks = bookService.getAllBooks();
        if (!allBooks.isEmpty()){
            return new ResponseEntity<List<Book>>(
                    allBooks,
                    headersGenerator.getHeaderForSuccessGetMethod(),
                    HttpStatus.OK
            );
        }
        return new ResponseEntity<List<Book>>(
                headersGenerator.getHeadersForError(),
                HttpStatus.BAD_REQUEST
        );
    }

    @GetMapping("/book/{id}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable int id, HttpServletRequest request){
        try {
            Book book = bookService.getBookById(id);
            BookResponse bookResponse = bookService.mapToBookResponse(book);
            return new ResponseEntity<BookResponse>(
                    bookResponse,
                    headersGenerator.getHeaderForSuccessGetMethod(),
                    HttpStatus.OK
            );
        }catch (Exception ex){
            log.error(ex.getMessage());
            return new ResponseEntity<>(
                    headersGenerator.getHeadersForError(),
                    HttpStatus.NOT_FOUND
            );
        }

    }

    @DeleteMapping("/deleteBook/{id}")
    public ResponseEntity<Book> deleteBookById(@PathVariable int id, HttpServletRequest request){
        Book bookById = bookService.getBookById(id);
        if (bookById != null){
            bookService.deleteBookById(id);
            return new ResponseEntity<Book>(
                    headersGenerator.getHeaderForSuccessGetMethod(),
                    HttpStatus.OK
            );

        }
        return new ResponseEntity<Book>(
                headersGenerator.getHeadersForError(),
                HttpStatus.BAD_REQUEST
        );

    }




}
