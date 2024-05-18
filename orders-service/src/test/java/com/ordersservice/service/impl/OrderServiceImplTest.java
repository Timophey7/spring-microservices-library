package com.ordersservice.service.impl;

import com.ordersservice.models.CartResponse;
import com.ordersservice.models.OrderInfo;
import com.ordersservice.repository.OrderInfoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderInfoRepository orderInfoRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    OrderInfo orderInfo;

    CartResponse cartResponse;

    @BeforeEach
    void setUp() {
        orderInfo = new OrderInfo();
        orderInfo.setOrderId(1);
        orderInfo.setNumberOfOrder("test111");
        cartResponse = new CartResponse();
        cartResponse.setCartId(1);
        cartResponse.setResultSum(100);
    }

    @Test
    void saveOrderInfo() {
        when(orderInfoRepository.save(orderInfo)).thenReturn(orderInfo);

        orderService.saveOrderInfo(orderInfo);

        verify(orderInfoRepository, times(1)).save(orderInfo);

    }

    @Test
    void createOrderInfo() {
        OrderInfo orderServiceOrderInfo = orderService.createOrderInfo(cartResponse);

        assertEquals(cartResponse.getCartId(), orderServiceOrderInfo.getCartId());
        assertEquals(cartResponse.getResultSum(), orderServiceOrderInfo.getResultSum());


    }

    @Test
    void getAllOrderInfo() {
        List<OrderInfo> allOrderInfo = List.of(orderInfo);
        when(orderInfoRepository.findAll()).thenReturn(allOrderInfo);

        List<OrderInfo> orderInfoList = orderService.getAllOrderInfo();
        assertEquals(allOrderInfo, orderInfoList);
        verify(orderInfoRepository).findAll();


    }

    @Test
    void getOrderInfoByOrderNumber() {
        String orderNumber = "test111";
        when(orderInfoRepository.findByNumberOfOrder(orderNumber)).thenReturn(Optional.of(orderInfo));

        OrderInfo orderInfoByOrderNumber = orderService.getOrderInfoByOrderNumber(orderNumber);

        assertEquals(orderInfo, orderInfoByOrderNumber);


    }
}