package com.example.apigatewayservice.filter

import com.example.apigatewayservice.LazyLogging
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class GlobalFilter: AbstractGatewayFilterFactory<GlobalFilter.Config>(Config::class.java), LazyLogging {

    override fun apply(config: Config): GatewayFilter {
        return GatewayFilter { exchange: ServerWebExchange, chain: GatewayFilterChain ->
            val request = exchange.request
            val response = exchange.response

            logger.info("Global Filter baseMessage: {}", config.baseMessage)
            if (config.preLogger){
                logger.info("Global Filter start: request id -> {}", request.id)
            }
            chain.filter(exchange).then(Mono.fromRunnable {
                if (config.postLogger) {
                    logger.info("Global Filter End: response code -> {}", response.statusCode)
                }
            })
        }
    }

    data class Config (
        val baseMessage: String,
        val preLogger: Boolean,
        val postLogger:Boolean
    )
}