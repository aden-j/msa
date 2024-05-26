package com.example.userservice.vo

import java.time.Instant

data class ResponseOrder(
    val productId: String,
    val qty: Int,
    val unitPrice: Int,
    val totalPrice: Int,
    val createdAt: Instant,
    val orderId: String
)
