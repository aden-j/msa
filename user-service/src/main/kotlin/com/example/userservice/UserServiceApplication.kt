package com.example.userservice

import com.example.userservice.error.FeignErrorDecoder
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.client.RestTemplate

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
class UserServiceApplication{
    @Bean
    fun bCryptPasswordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

//    @Bean
//    @LoadBalanced
//    fun restTemplate(): RestTemplate {
//        return RestTemplate()
//    }

    @Bean
    fun feignLoggerLevel(): feign.Logger.Level {
        return feign.Logger.Level.FULL
    }

//    @Bean
//    fun feignErrorDecoder(): FeignErrorDecoder {
//        return FeignErrorDecoder()
//    }
}

fun main(args: Array<String>) {
    runApplication<UserServiceApplication>(*args)
}
