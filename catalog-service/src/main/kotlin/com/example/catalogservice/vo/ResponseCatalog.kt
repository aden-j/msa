package com.example.catalogservice.vo

import java.time.Instant

data class ResponseCatalog(
    val productId: String,
    val productName: String,
    val stock: Int,
    val unitPrice: Int,
    val createdAt: Instant?
)
