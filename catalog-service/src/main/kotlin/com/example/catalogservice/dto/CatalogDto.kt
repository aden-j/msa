package com.example.catalogservice.dto

import java.io.Serializable

data class CatalogDto(
    val productId: String,
    val qty: Int,
    val unitPrice: Int,
    val totalPrice: Int,

    val orderId: String,
    val userId: String
): Serializable

