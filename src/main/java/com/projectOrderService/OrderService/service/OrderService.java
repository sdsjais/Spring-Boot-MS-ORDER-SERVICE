package com.projectOrderService.OrderService.service;

import com.projectOrderService.OrderService.model.OrderRequest;

public interface OrderService {
    long placeOrder(OrderRequest orderRequest);
}
