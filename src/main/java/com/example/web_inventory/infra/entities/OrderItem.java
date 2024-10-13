package com.example.web_inventory.infra.entities;

import java.math.BigInteger;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.Data;

@Table(name="order-items")
@Entity(name="order-items")
@Data
public class OrderItem {
    
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Order order;

    @Column(nullable = false)
    private Product product;

    @Column(nullable = false, length = 5)
    private Integer quantity;

    @Column(nullable = false, length = 15)
    private BigInteger unitPrice;
    
}