package com.ordersservice.controllers;

import com.ordersservice.headers.HeadersGenerator;
import com.ordersservice.models.OrderInfo;
import com.ordersservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/user/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final HeadersGenerator headersGenerator;

    private final OrderService orderService;

    @GetMapping("/getAllOrders")
    public ResponseEntity<List<OrderInfo>> getAllOrders() {

        List<OrderInfo> allOrderInfo = orderService.getAllOrderInfo();
        if (allOrderInfo.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(
                allOrderInfo,
                headersGenerator.getHeaderForSuccessGetMethod(),
                HttpStatus.OK
        );

    }

    @GetMapping("/getOrderByOrderNumber/{numberOfOrder}")
    public ResponseEntity<OrderInfo> getOrderByOrderNumber(
            @PathVariable("numberOfOrder") String numberOfOrder
    ) {
        OrderInfo orderInfoByOrderNumber = orderService.getOrderInfoByOrderNumber(numberOfOrder);
        if (orderInfoByOrderNumber == null) {
            return new ResponseEntity<>(
                    headersGenerator.getHeadersForError(),
                    HttpStatus.NOT_FOUND
            );
        }
        return new ResponseEntity<>(orderInfoByOrderNumber, HttpStatus.OK);
    }

}
