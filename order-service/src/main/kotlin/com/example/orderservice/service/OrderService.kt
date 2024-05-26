package com.example.orderservice.service

import com.example.orderservice.dto.OrderDto
import com.example.orderservice.jpa.OrderEntity

interface OrderService {
    fun createOrder(orderDetails: OrderDto): OrderDto
    fun getOrderByOrderId(orderId: String): OrderDto
    fun getOrdersByUserId(userId: String): List<OrderEntity>
}