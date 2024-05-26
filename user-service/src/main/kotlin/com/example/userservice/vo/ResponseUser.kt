package com.example.userservice.vo

data class ResponseUser(
    val email: String,
    val name: String,
    val userId: String?,
    val orders: List<ResponseOrder>
)
