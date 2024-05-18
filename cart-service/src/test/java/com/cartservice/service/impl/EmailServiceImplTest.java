package com.cartservice.service.impl;

import io.jsonwebtoken.Claims;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(EmailServiceImpl.class)
class EmailServiceImplTest {
    @Mock
    private JavaMailSender mailSender;

    @Mock
    private MimeMessageHelper mimeMessageHelper;

    @InjectMocks
    private EmailServiceImpl emailService;

    @BeforeEach
    void setUp() {
        mimeMessageHelper = Mockito.mock(MimeMessageHelper.class);
        mailSender = Mockito.mock(JavaMailSender.class);
        emailService = new EmailServiceImpl(mimeMessageHelper, mailSender);
    }


    @Test
    void sendEmail() throws Exception {

        Long cartId = 1L;
        int resultSum = 100;
        String email = "test@gmail.com";

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);


        emailService.sendEmail(cartId, resultSum, email);

        verify(mimeMessageHelper).setFrom("pizza@gmail.com");
        verify(mimeMessageHelper).setText("you create order it cost " + resultSum + " and your cart id :" + cartId, true);
        verify(mimeMessageHelper).setTo(email);
        verify(mimeMessageHelper).setSubject("you create order");
        verify(mailSender).send(mimeMessageHelper.getMimeMessage());

    }

    @Test
    void getEmailFromToken() {
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbkBnbWFpbC5jb20iLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ak-J2k9289OO3TeZn2zU4BISpm9pwES9Urn7AlhGus4";
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getHeader("Authorization")).thenReturn("Bearer "+token);

        String email = emailService.getEmailFromToken(request);

        assertEquals("admin@gmail.com", email);


    }
}