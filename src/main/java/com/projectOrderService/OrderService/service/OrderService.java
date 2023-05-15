package com.projectOrderService.OrderService.service;

import com.projectOrderService.OrderService.model.OrderRequest;
import com.projectOrderService.OrderService.model.OrderResponse;

public interface OrderService {
    long placeOrder(OrderRequest orderRequest);

    OrderResponse getOrderDetails(long orderId);
}
