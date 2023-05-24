package com.projectOrderService.OrderService.external.client;

import com.projectOrderService.OrderService.exception.CustomException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "PRODUCT-SERVICE/product")
@CircuitBreaker(name = "external",fallbackMethod = "fallback")
public interface ProductService {

    @PutMapping("/reduceQuantity/{id}")
    ResponseEntity<Void> reduceQuantity(@PathVariable("id") long productId, @RequestParam("quantity") long quantity);

    default void fallback(Exception e){
        throw new CustomException("Product Service is down", "UNAVAILABLE", 500);
    }
}
