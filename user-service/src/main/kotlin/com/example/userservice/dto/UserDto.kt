package com.example.userservice.dto

import com.example.userservice.vo.ResponseOrder
import java.time.Instant

data class UserDto(
    val email: String,
    val name: String,
    var pwd: String?,
    val userId: String? = null,
    val createdAt: Instant? = null,
    var decryptedPwd: String? = null,
    val encryptedPwd: String? = null,
    val orders: List<ResponseOrder> = emptyList()
)
