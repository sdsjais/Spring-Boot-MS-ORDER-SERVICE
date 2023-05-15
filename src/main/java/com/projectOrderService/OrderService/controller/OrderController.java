package com.projectOrderService.OrderService.controller;

import com.projectOrderService.OrderService.model.OrderRequest;
import com.projectOrderService.OrderService.model.OrderResponse;
import com.projectOrderService.OrderService.service.OrderService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@Log4j2
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/placeOrder")
    public ResponseEntity<Long> placeOrder(@RequestBody OrderRequest orderRequest){
        long OrderId = orderService.placeOrder(orderRequest);
        log.info("Order Id:{}",OrderId);
        return new ResponseEntity<>(OrderId, HttpStatus.CREATED);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable("orderId") long orderId){
        return new ResponseEntity<>(orderService.getOrderDetails(orderId),HttpStatus.OK);
    }


}
