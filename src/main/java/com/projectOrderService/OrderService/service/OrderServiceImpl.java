package com.projectOrderService.OrderService.service;

import com.projectOrderService.OrderService.entity.Order;
import com.projectOrderService.OrderService.exception.CustomException;
import com.projectOrderService.OrderService.external.Request.PaymentRequest;
import com.projectOrderService.OrderService.external.client.PaymentService;
import com.projectOrderService.OrderService.external.client.ProductService;
import com.projectOrderService.OrderService.model.OrderRequest;
import com.projectOrderService.OrderService.model.OrderResponse;
import com.projectOrderService.OrderService.model.PaymentResponse;
import com.projectOrderService.OrderService.model.ProductResponse;
import com.projectOrderService.OrderService.repository.OrderRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Service
@Log4j2
public class OrderServiceImpl implements OrderService{

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private RestTemplate restTemplate;

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

        log.info("Calling Payment Service for Payment");

        PaymentRequest paymentRequest = PaymentRequest.builder()
                .orderId(order.getId())
                .paymentMode(orderRequest.getPaymentMode())
                .amount(orderRequest.getTotalAmount())
                .build();

        String orderStatus = null;
        try{
            paymentService.doPayment(paymentRequest);
            log.info("Payment Successfull. Changing orderStatus to Placed");
            orderStatus = "PLACED";
        }catch (Exception e){
            log.info("Payment Failed. Changing order status to PAYMENT_FAILED");
            orderStatus = "PAYMENT_FAILED";
        }

        order.setOrderStatus(orderStatus);
        orderRepository.save(order);

        log.info("order saved with orderId:{}", order.getId());


        return order.getId();
    }

    @Override
    public OrderResponse getOrderDetails(long orderId) {
        log.info("Get order details for Order Id:: {}", orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(()-> new CustomException("Order not found of order Id "+ orderId, "NOT_FOUND",404));

        log.info("Invoking Product Service to fetch the product for id : {}", order.getProductId());

        ProductResponse productResponse = restTemplate.getForObject(
                "https://PRODUCT-SERVICE/product/"+order.getProductId(),
                ProductResponse.class
        );

        log.info("fetching Payment details");

        PaymentResponse paymentResponse
                = restTemplate.getForObject(
                        "https://PAYMENT-SERVICE/payment/order/"+order.getId(),
                PaymentResponse.class
        );

        OrderResponse.ProductDetails productDetails = OrderResponse.ProductDetails.builder()
                .productName(productResponse.getProductName())
                .productId(productResponse.getProductId())
                .build();

        OrderResponse.PaymentDetails paymentDetails
                = OrderResponse.PaymentDetails.builder()
                .paymentMode(paymentResponse.getPaymentMode())
                .paymentDate(paymentResponse.getPaymentDate())
                .paymentId(paymentResponse.getPaymentId())
                .status(paymentResponse.getStatus()).build();

        OrderResponse orderResponse = OrderResponse.builder()
                .orderId(order.getId())
                .orderDate(order.getOrderDate())
                .orderStatus(order.getOrderStatus())
                .amount(order.getAmount())
                .productDetails(productDetails)
                .paymentDetails(paymentDetails)
                .build();
        return orderResponse;
    }
}
