package com.example.userservice.jpa

import org.springframework.data.repository.CrudRepository

interface UserRepository: CrudRepository<UserEntity, Long> {

    fun findByEmail(email: String): UserEntity?
    fun findByUserId(userId: String): UserEntity?
}