package com.nastyzera.OrderService.service;

import com.nastyzera.OrderService.model.OrderRequest;
import com.nastyzera.OrderService.model.OrderResponse;

public interface OrderService {

    long placeOrder(OrderRequest orderRequest);

    OrderResponse getOrderDetails(long orderId);
    
}
