package com.example.apigatewayservice.filter

import com.example.apigatewayservice.LazyLogging
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.core.Ordered
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono


@Component
class LoggingFilter: AbstractGatewayFilterFactory<LoggingFilter.Config>(Config::class.java), LazyLogging {

    override fun apply(config: Config): GatewayFilter {
        val filter: GatewayFilter = OrderedGatewayFilter({ exchange: ServerWebExchange, chain: GatewayFilterChain ->
            val request = exchange.request
            val response = exchange.response

            logger.info("Logging Filter baseMessage: {}", config.baseMessage)
            if (config.preLogger) {
                logger.info("Logging PRE Filter: request id -> {}", request.id)
            }
            chain.filter(exchange).then<Void>(Mono.fromRunnable<Void> {
                if (config.postLogger) {
                    logger.info("Logging POST Filter: response code -> {}", response.statusCode)
                }
            })
        }, Ordered.LOWEST_PRECEDENCE)

        return filter
    }

    data class Config (
        val baseMessage: String,
        val preLogger: Boolean,
        val postLogger:Boolean
    )
}