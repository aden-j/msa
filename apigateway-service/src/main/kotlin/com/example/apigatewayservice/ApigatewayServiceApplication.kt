package com.example.apigatewayservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.boot.actuate.web.exchanges.HttpExchangeRepository
import org.springframework.boot.actuate.web.exchanges.InMemoryHttpExchangeRepository

@SpringBootApplication
class ApigatewayServiceApplication {
    @Bean
    fun httpTraceRepository(): HttpExchangeRepository {
        return InMemoryHttpExchangeRepository()
    }
}

fun main(args: Array<String>) {
    runApplication<ApigatewayServiceApplication>(*args)

}


