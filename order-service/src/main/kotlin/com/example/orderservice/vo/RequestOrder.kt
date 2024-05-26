package com.example.orderservice.vo

import java.time.Instant

data class RequestOrder(
    val productId: String,
    val qty: Int,
    val unitPrice: Int,
)
