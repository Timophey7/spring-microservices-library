package com.library.models;

import lombok.Data;

import java.io.Serializable;

@Data
public class BookResponse implements Serializable {
    private int id;
    private String title;
    private String author;
    private String isbn;
    private String publisher;
    private String publishedDate;
    private String description;
    private String photoUrl;
    private int price;
}

