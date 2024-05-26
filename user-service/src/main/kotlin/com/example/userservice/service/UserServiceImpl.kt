package com.example.userservice.service

import com.example.userservice.dto.UserDto
import com.example.userservice.jpa.UserEntity
import com.example.userservice.jpa.UserRepository
import com.example.userservice.jpa.toEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.*


@Service
class UserServiceImpl(
    @Autowired val userRepository: UserRepository,
    @Autowired val passwordEncoder: BCryptPasswordEncoder
): UserService {
    override fun createUser(user: UserDto): UserDto {
        return userRepository.save(
            user.toEntity(UUID.randomUUID().toString(), passwordEncoder.encode(user.pwd))
        ).toModel()
    }

    override fun loadUserByUsername(username: String): UserDetails {
        val userEntity: UserEntity = userRepository.findByEmail(username)
            ?: throw UsernameNotFoundException("$username: not found")
        println("####")
        println(userEntity)
        return User(
            userEntity.email, userEntity.encryptedPwd,
            true, true, true, true,
            ArrayList()
        )
    }

    override fun getUserByUserId(userId: String): UserDto {
        val user = userRepository.findByUserId(userId) ?: throw UsernameNotFoundException("User not found")
        return user.toModel()
    }

    override fun getUserAll(): Iterable<UserDto> {
        return userRepository.findAll().map { it.toModel() }
    }

    override fun getUserDetailsByEmail(email: String): UserDto {
        return userRepository.findByEmail(email)?.toModel() ?: throw UsernameNotFoundException("User not found")
    }
}