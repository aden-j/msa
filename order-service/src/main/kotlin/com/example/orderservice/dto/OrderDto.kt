package com.example.orderservice.dto

import java.io.Serializable

data class OrderDto(
    val productId: String,
    val qty: Int,
    val unitPrice: Int,
    val totalPrice: Int,

    val orderId: String,
    val userId: String
): Serializable

