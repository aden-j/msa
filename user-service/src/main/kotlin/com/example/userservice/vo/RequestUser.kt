package com.example.userservice.vo

data class RequestUser(
    val email: String,
    val pwd: String,
    val name: String,

    val orders: List<ResponseOrder> = emptyList()
)
