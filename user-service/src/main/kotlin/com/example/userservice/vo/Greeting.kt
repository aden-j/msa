package com.example.userservice.vo

import org.springframework.beans.factory.annotation.Value

data class Greeting(
    @Value("\${greeting.message}")
    val message: String
)
