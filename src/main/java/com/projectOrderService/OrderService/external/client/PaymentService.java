package com.projectOrderService.OrderService.external.client;

import com.projectOrderService.OrderService.exception.CustomException;
import com.projectOrderService.OrderService.external.Request.PaymentRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "PAYMENT-SERVICE/payment")
@CircuitBreaker(name = "external",fallbackMethod = "fallback")
public interface PaymentService {
    @PostMapping("/doPayment")
    public ResponseEntity<Long> doPayment(@RequestBody PaymentRequest paymentRequest);

    default void fallback(Exception e){
        throw new CustomException("Payment Service is down", "UNAVAILABLE",500);
    }
}
