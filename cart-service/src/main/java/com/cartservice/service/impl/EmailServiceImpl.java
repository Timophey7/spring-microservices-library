package com.cartservice.service.impl;

import com.cartservice.service.EmailService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.security.Key;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private static final String SECRET_KEY = "d4a6c9be06f07a705e174c276a0d8e87d5fc96dc0b8d01f06a341a72b0c5644864436855387a42beb9679a5116913e357be1dff5acf246bd1447aaca74e92aa13c97d8968e017170950404572e6d0fb4f9d1567c2eb7ac0596b046036e77f78974e96d5fb7b69c6e11a3ee70dc1390ae9e3c8f3b7fc6f4e6b0161f5d1486bcdec439eeca9ab700b9077112acf2206cd115fb9dfcd840c77fd458a961a5906b1d6c37edf8c6333d5fff88ea4d4eb034f980bc0d3892de2a174bac82126e9361c9460b6c1b31e87b7430e7083d8dfbb5e693f6b21dc67a5b80ce6d61c95c3687528718329b453f268b94f0a1d1cc1ed6825e54a06b072c8726b381f69cc3db77a5";
    private final MimeMessageHelper helper;

    private final JavaMailSender mailSender;


    @Override
    public void sendEmail(Long cartId, int resultSum, String email) {
        try {
            helper.setFrom("pizza@gmail.com");
            helper.setText("you create order it cost " + resultSum + " and your cart id :"+cartId,true);
            helper.setTo(email);
            log.info("email sent to "+email);
            helper.setSubject("you create order");
            mailSender.send(helper.getMimeMessage());
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
        }

    }

    @Override
    public String getEmailFromToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            Claims claims = Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(jwtToken).getBody();
            String email = claims.getSubject();

            return email;
        }

        return null;
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
