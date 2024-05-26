package com.example.apigatewayservice.config

import com.example.apigatewayservice.LazyLogging
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono



@Component
class CustomFilter: AbstractGatewayFilterFactory<CustomFilter.Config>(Config::class.java), LazyLogging {
    override fun apply(config: Config?): GatewayFilter {
        return GatewayFilter { exchange: ServerWebExchange, chain: GatewayFilterChain ->
            val request = exchange.request
            val response = exchange.response

            logger.info("Custom PRE filter: request id -> {}", request.id)
            chain.filter(exchange).then(Mono.fromRunnable {
                logger.info("Custom POST filter: response code -> {}", response.statusCode)
            })
        }
    }

    class Config
}