package com.example.userservice.jpa

import com.example.userservice.dto.UserDto
import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "users")
data class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(length = 50, unique = true)
    val email: String = "",

    @Column(length = 50)
    val name: String = "",

    @Column(unique = true)
    val userId: String? = null,

    @Column(unique = true)
    val encryptedPwd: String? = null,

    @Column(unique = true)
    val createdAt: Instant? = null,
){
    fun toModel() = UserDto(
        email = email,
        name = name,
        pwd = encryptedPwd,
        userId = userId,
        createdAt = createdAt,
        encryptedPwd = encryptedPwd,
        decryptedPwd = encryptedPwd,
    )
}

fun UserDto.toEntity(userId: String, pw: String) = UserEntity(
    id = null,
    email = email,
    name = name,
    userId = userId,
    encryptedPwd = pw,
    createdAt = Instant.now()

)
