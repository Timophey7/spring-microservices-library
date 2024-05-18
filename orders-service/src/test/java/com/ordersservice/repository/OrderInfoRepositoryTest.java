package com.ordersservice.repository;

import com.ordersservice.models.OrderInfo;
import com.ordersservice.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class OrderInfoRepositoryTest {

    @Autowired
    private OrderInfoRepository orderInfoRepository;


    @Test
    void findByNumberOfOrder() {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderId(1);
        orderInfo.setNumberOfOrder("test111");
        orderInfoRepository.save(orderInfo);

        String number = "test111";

        Optional<OrderInfo> byNumberOfOrder = orderInfoRepository.findByNumberOfOrder(number);

        assertEquals(byNumberOfOrder.get().getNumberOfOrder(), orderInfo.getNumberOfOrder());
        assertEquals(byNumberOfOrder.get().getOrderId(), orderInfo.getOrderId());


    }
}