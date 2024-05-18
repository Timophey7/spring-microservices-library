package com.ordersservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ordersservice.headers.HeadersGenerator;
import com.ordersservice.models.OrderInfo;
import com.ordersservice.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    @MockBean
    private HeadersGenerator headersGenerator;

    OrderInfo orderInfo;

    @BeforeEach
    void setUp() {
        orderInfo = new OrderInfo();
        orderInfo.setCartId(1);
        orderInfo.setOrderId(1);
        orderInfo.setNumberOfOrder("test111");
        orderInfo.setResultSum(100);
    }

    @Test
    void getAllOrdersShouldReturnStatusIsOk() throws Exception {
        List<OrderInfo> allOrderInfo = List.of(orderInfo);
        when(orderService.getAllOrderInfo()).thenReturn(allOrderInfo);

        ResultActions response = mockMvc.perform(get("/v1/user/order/getAllOrders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(allOrderInfo))
        );

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$..cartId").value(1))
                .andExpect(jsonPath("$..numberOfOrder").value("test111"))
                .andExpect(jsonPath("$..resultSum").value(100));


    }

    @Test
    void getOrderNoContent() throws Exception {
        List<OrderInfo> allOrderInfo = List.of();
        when(orderService.getAllOrderInfo()).thenReturn(allOrderInfo);

        ResultActions response = mockMvc.perform(get("/v1/user/order/getAllOrders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(allOrderInfo))
        );

        response.andExpect(status().isNoContent());
    }


    @Test
    void getOrderByOrderNumberShouldReturnStatusIsOk() throws Exception {
        String orderNumber = "test111";
        when(orderService.getOrderInfoByOrderNumber(orderNumber)).thenReturn(orderInfo);

        ResultActions response = mockMvc.perform(get("/v1/user/order/getOrderByOrderNumber/"+orderNumber)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderInfo))
        );

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.cartId").value(1))
                .andExpect(jsonPath("$.numberOfOrder").value("test111"))
                .andExpect(jsonPath("$.resultSum").value(100));

    }

    @Test
    void getOrderByOrderNumberShouldReturnStatusIsNotFound() throws Exception {
        String orderNumber = "test111";
        when(orderService.getOrderInfoByOrderNumber(orderNumber)).thenReturn(null);

        ResultActions response = mockMvc.perform(get("/v1/user/order/getOrderByOrderNumber/"+orderNumber)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderInfo))
        );

        response.andExpect(status().isNotFound());
    }
}