package com.ordersservice.service.impl;

import com.ordersservice.models.CartResponse;
import com.ordersservice.models.OrderInfo;
import com.ordersservice.repository.OrderInfoRepository;
import com.ordersservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderInfoRepository orderInfoRepository;

    @Override
    public void saveOrderInfo(OrderInfo orderInfo) {
        orderInfoRepository.save(orderInfo);
    }

    @Override
    public OrderInfo createOrderInfo(CartResponse cartResponse) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo = OrderInfo.builder()
                .cartId(cartResponse.getCartId())
                .resultSum(cartResponse.getResultSum())
                .dateOfOrder(new Date())
                .numberOfOrder(UUID.randomUUID().toString().substring(0,7))
                .build();
        return orderInfo;
    }

    @Override
    public List<OrderInfo> getAllOrderInfo() {
        return orderInfoRepository.findAll();
    }

    @Override
    public OrderInfo getOrderInfoByOrderNumber(String numberOfOrder) {
        OrderInfo orderInfo = orderInfoRepository.findByNumberOfOrder(numberOfOrder).orElse(null);
        return orderInfo;
    }
}
