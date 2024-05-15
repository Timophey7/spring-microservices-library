package com.cartservice.service.impl;

import com.cartservice.models.CartResponse;
import com.cartservice.service.KafkaDataService;
import lombok.RequiredArgsConstructor;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class KafkaDataServiceImpl implements KafkaDataService {


    private final KafkaTemplate<String,CartResponse> kafkaTemplate;

    @Override
    public void send(CartResponse cartResponse) {
        kafkaTemplate.send("cart",cartResponse);
    }
}
