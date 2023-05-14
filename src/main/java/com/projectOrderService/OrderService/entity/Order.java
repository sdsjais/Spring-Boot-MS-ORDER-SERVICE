package com.projectOrderService.OrderService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;


@Entity
@Table(name="TABLE_DETAILS")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "PRODUCT_ID")
    private long productId;

    @Column(name = "QUANTITY")
    private long quantity;

    @Column(name = "ORDER_DATE")
    private Instant OrderDate;

    @Column(name = "ORDER_SERVICE")
    private String orderStatus;

    @Column(name = "TOTAL_AMOUNT")
    private long amount;
}
