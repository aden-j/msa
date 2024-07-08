package com.example.userservice.service

import com.example.userservice.LazyLogging
import com.example.userservice.client.OrderServiceClient
import com.example.userservice.dto.UserDto
import com.example.userservice.jpa.UserEntity
import com.example.userservice.jpa.UserRepository
import com.example.userservice.jpa.toEntity
import com.example.userservice.vo.ResponseOrder
import feign.FeignException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.ParameterizedTypeReference
import org.springframework.core.env.Environment
import org.springframework.http.HttpMethod
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange
import java.util.*


@Service
class UserServiceImpl(
    @Autowired val userRepository: UserRepository,
    @Autowired val passwordEncoder: BCryptPasswordEncoder,
    //@Autowired val restTemplate: RestTemplate,
    @Autowired val orderServiceClient: OrderServiceClient,
    @Autowired val env: Environment
): UserService, LazyLogging {
    override fun createUser(user: UserDto): UserDto {
        return userRepository.save(
            user.toEntity(UUID.randomUUID().toString(), passwordEncoder.encode(user.pwd))
        ).toModel(null)
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
//        println(user)
//        val orderUrl = String.format(env.getProperty("order_service.url") ?: "", userId)
//        println(orderUrl)
//        val responseType = object: ParameterizedTypeReference<List<ResponseOrder>>() {}
//        val orderList = restTemplate.exchange(orderUrl, HttpMethod.GET, null, responseType).body
//        var orderList: List<ResponseOrder>? = null
//        try {
//            orderList = orderServiceClient.getOrders(userId)
//        } catch (ex: FeignException){
//            logger.error(ex.message)
//        }
        val orderList = orderServiceClient.getOrders(userId)
        //println(orderList)
        return user.toModel(orderList)
    }

    override fun getUserAll(): Iterable<UserDto> {
        return userRepository.findAll().map { it.toModel(null) }
    }

    override fun getUserDetailsByEmail(email: String): UserDto {
        return userRepository.findByEmail(email)?.toModel(null) ?: throw UsernameNotFoundException("User not found")
    }
}