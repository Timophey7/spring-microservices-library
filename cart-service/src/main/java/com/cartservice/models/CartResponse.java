package com.cartservice.models;

import lombok.Builder;
import lombok.Data;

@Data
public class CartResponse {

    private Long cartId;
    private int resultSum;

}
