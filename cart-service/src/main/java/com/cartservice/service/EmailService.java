package com.cartservice.service;

import jakarta.servlet.http.HttpServletRequest;

public interface EmailService {

    void sendEmail(Long cartId,int resultSum,String email);

    String getEmailFromToken(HttpServletRequest request);
}
