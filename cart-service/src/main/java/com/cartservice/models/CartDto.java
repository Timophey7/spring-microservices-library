package com.cartservice.models;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class CartDto {
    private Long id;
    private List<Integer> productIds;
}
