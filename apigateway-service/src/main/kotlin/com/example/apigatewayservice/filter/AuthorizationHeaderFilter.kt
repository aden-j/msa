package com.example.apigatewayservice.filter

import com.example.apigatewayservice.LazyLogging
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.apache.http.HttpHeaders
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.core.env.Environment
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.nio.charset.StandardCharsets
import java.security.Key
import java.util.*


@Component
class AuthorizationHeaderFilter(
    @Autowired private val env: Environment
): AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config>(Config::class.java), LazyLogging {
    class Config

    override fun apply(config: Config?): GatewayFilter? {
        return GatewayFilter { exchange: ServerWebExchange, chain: GatewayFilterChain ->
            val request = exchange.request
            val response = exchange.response

            if (!request.headers.containsKey(HttpHeaders.AUTHORIZATION)) {
                onError(exchange, "No autorization header", HttpStatus.UNAUTHORIZED)
            }

            else {
                val authorizationHeader = request.headers[HttpHeaders.AUTHORIZATION]?.get(0)
                val jwt = authorizationHeader!!.replace("Bearer", "")

                if (!isJwtValid(jwt)) {
                    onError(exchange, "JWT is not valid", HttpStatus.UNAUTHORIZED)
                }

                chain.filter(exchange).then(Mono.fromRunnable {

                })
            }
        }
    }

    private fun onError(exchange: ServerWebExchange, err: String, httpStatus: HttpStatus): Mono<Void> {
        val response = exchange.response
        response.setStatusCode(httpStatus)
        logger.error(err)

        val bytes: ByteArray = "The requested token is invalid.".toByteArray(StandardCharsets.UTF_8)
        val buffer: DataBuffer = exchange.response.bufferFactory().wrap(bytes)
        return response.writeWith(Flux.just(buffer))

    }

    private fun isJwtValid(jwt: String): Boolean {
        var returnValue = true

        var subject: String? = null
        val secretKeyBytes = Base64.getEncoder().encode(env.getProperty("token.secret")!!.toByteArray())

        val secretKey = Keys.hmacShaKeyFor(secretKeyBytes)

        try {
            subject = Jwts
                .parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(jwt).body
                .subject
        } catch (ex: Exception) {
            returnValue = false
        }

        if (subject == null || subject.isEmpty()) {
            returnValue = false
        }

        return returnValue
    }


}