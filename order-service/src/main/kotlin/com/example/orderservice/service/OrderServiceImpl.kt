package com.example.orderservice.service

import com.example.orderservice.dto.OrderDto
import com.example.orderservice.jpa.OrderEntity
import com.example.orderservice.jpa.OrderRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

@Service
class OrderServiceImpl(
    @Autowired val orderRepository: OrderRepository
): OrderService {
    override fun createOrder(orderDetails: OrderDto): OrderDto {
        //val userId = UUID.randomUUID().toString()
        return orderRepository.save(
            OrderEntity(
                id = null,
                productId = orderDetails.productId,
                qty = orderDetails.qty,
                unitPrice = orderDetails.unitPrice,
                totalPrice = orderDetails.unitPrice * orderDetails.qty,
                userId = orderDetails.userId,
                orderId = orderDetails.orderId,
                createdAt = Instant.now()
            )
        ).toDto()
    }

    override fun getOrderByOrderId(orderId: String): OrderDto {
        return orderRepository.findByOrderId(orderId).toDto()
    }

    override fun getOrdersByUserId(userId: String): List<OrderEntity> {
        return orderRepository.findAllByUserId(userId)
    }
}