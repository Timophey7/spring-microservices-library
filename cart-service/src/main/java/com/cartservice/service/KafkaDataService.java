package com.cartservice.service;

import com.cartservice.models.CartResponse;

public interface KafkaDataService {

    void send(CartResponse cartResponse);


}
