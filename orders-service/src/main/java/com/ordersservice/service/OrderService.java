package com.ordersservice.service;

import com.ordersservice.models.CartResponse;
import com.ordersservice.models.OrderInfo;
import jakarta.transaction.Transactional;

import java.util.List;

public interface OrderService {

    @Transactional
    void saveOrderInfo(OrderInfo orderInfo);

    OrderInfo createOrderInfo(CartResponse cartResponse);

    List<OrderInfo> getAllOrderInfo();

    OrderInfo getOrderInfoByOrderNumber(String numberOfOrder);
}
