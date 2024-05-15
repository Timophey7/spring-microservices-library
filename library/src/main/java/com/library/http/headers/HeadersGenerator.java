package com.library.http.headers;

import com.library.models.Book;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpHeaders;

import java.net.URI;
import java.net.URISyntaxException;


@Service
public class HeadersGenerator {

    public HttpHeaders getHeaderForSuccessGetMethod(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return headers;
    }

    public HttpHeaders getHeadersForError(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/problem+json; charset=UTF-8");
        return headers;
    }

    public HttpHeaders getHeadersForSuccessPostMethod() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json; charset=UTF-8");
        return httpHeaders;
    }


}
