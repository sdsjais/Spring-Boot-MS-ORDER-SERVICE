package com.projectOrderService.OrderService.service;

import com.projectOrderService.OrderService.entity.Order;
import com.projectOrderService.OrderService.external.client.ProductService;
import com.projectOrderService.OrderService.model.OrderRequest;
import com.projectOrderService.OrderService.repository.OrderRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Log4j2
public class OrderServiceImpl implements OrderService{

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    @Override
    public long placeOrder(OrderRequest orderRequest) {
        log.info("Placing Order Request : {}",orderRequest);

        productService.reduceQuantity(orderRequest.getProductId(),orderRequest.getQuantity());

        log.info("Creating order with status: CREATED");
        Order order = Order.builder()
                .productId(orderRequest.getProductId())
                .OrderDate(Instant.now())
                .amount(orderRequest.getTotalAmount())
                .orderStatus("CREATED")
                .quantity(orderRequest.getQuantity()).build();

        order = orderRepository.save(order);
        log.info("order saved with orderId:{}", order.getId());

        return order.getId();
    }
}
