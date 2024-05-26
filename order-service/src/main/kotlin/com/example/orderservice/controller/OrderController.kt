package com.example.orderservice.controller

import com.example.orderservice.dto.OrderDto
import com.example.orderservice.service.OrderService
import com.example.orderservice.vo.RequestOrder
import com.example.orderservice.vo.ResponseOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.Instant
import java.util.UUID


@RestController
@RequestMapping("/order-service")
class OrderController(
    @Autowired val env: Environment,
    @Autowired val orderService: OrderService
) {

    @GetMapping("/health-check")
    fun status(): String {
        return "It's working in Order Service on PORT ${env.getProperty("local.server.port")}"
    }

    @PostMapping("/{userId}/orders")
    fun createOrder(
        @RequestBody order: RequestOrder,
        @PathVariable("userId") userId: String
    ): ResponseEntity<ResponseOrder> {
        val order = orderService.createOrder(
            OrderDto(
                productId = order.productId,
                qty = order.qty,
                unitPrice = order.unitPrice,
                totalPrice = order.unitPrice * order.qty,
                orderId = UUID.randomUUID().toString(),
                userId = userId
            )
        ).toModel()
        return ResponseEntity.status(HttpStatus.OK).body(order)
    }

    @GetMapping("/{userId}/orders")
    fun getOrder(
        @PathVariable("userId") userId: String
    ): ResponseEntity<List<ResponseOrder>> {
        val order = orderService.getOrdersByUserId(userId).map {
            it.toDto().toModel()
        }
        return ResponseEntity.status(HttpStatus.OK).body(order)
    }

}

fun OrderDto.toModel(): ResponseOrder {
    return ResponseOrder(
        productId = productId!!,
        qty = qty,
        unitPrice = unitPrice!!,
        totalPrice = totalPrice!!,
        createdAt = Instant.now(),
        orderId = orderId!!
    )
}