package com.example.orderservice.jpa

import com.example.orderservice.dto.OrderDto
import jakarta.persistence.*
import java.io.Serializable
import java.time.Instant

@Entity
@Table(name = "orders")
data class OrderEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "product_id", unique = true)
    val productId: String = "",

    @Column(name = "qty")
    val qty: Int = 0,

    @Column(name = "unit_price")
    val unitPrice: Int = 0,

    @Column(name = "total_price")
    val totalPrice: Int = 0,

    @Column(name = "user_id")
    val userId: String = "",

    @Column(name = "order_id")
    val orderId: String = "",

    @Column(name="created_at")
    val createdAt: Instant? = Instant.now(),
): Serializable {
    fun toDto() = OrderDto(
        productId = productId,
        qty = qty,
        unitPrice = unitPrice,
        totalPrice = totalPrice,
        orderId = orderId,
        userId = userId,
    )
}