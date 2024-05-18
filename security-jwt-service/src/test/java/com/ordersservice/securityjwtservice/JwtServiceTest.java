package com.ordersservice.securityjwtservice;

import com.ordersservice.securityjwtservice.models.User;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    private static final String SECRET_KEY = "d4a6c9be06f07a705e174c276a0d8e87d5fc96dc0b8d01f06a341a72b0c5644864436855387a42beb9679a5116913e357be1dff5acf246bd1447aaca74e92aa13c97d8968e017170950404572e6d0fb4f9d1567c2eb7ac0596b046036e77f78974e96d5fb7b69c6e11a3ee70dc1390ae9e3c8f3b7fc6f4e6b0161f5d1486bcdec439eeca9ab700b9077112acf2206cd115fb9dfcd840c77fd458a961a5906b1d6c37edf8c6333d5fff88ea4d4eb034f980bc0d3892de2a174bac82126e9361c9460b6c1b31e87b7430e7083d8dfbb5e693f6b21dc67a5b80ce6d61c95c3687528718329b453f268b94f0a1d1cc1ed6825e54a06b072c8726b381f69cc3db77a5";

    @InjectMocks
    JwtService jwtService;

    User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1);
        user.setFirstname("test");
        user.setLastname("test");
        user.setEmail("test@test.com");
        user.setPassword("test");
    }

    @Test
    void generateToken() {

        String token = jwtService.generateToken(user);

        assertNotNull(token);
    }


}