package com.example.userservice.error

import feign.Response
import feign.codec.ErrorDecoder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException

@Component
class FeignErrorDecoder(
    @Autowired private val env: Environment
): ErrorDecoder {

   override fun decode(methodKey: String?, response: Response?): Exception? {
       return if (response != null) {
           when(response.status()) {
               400 -> Exception(response.reason())
               403 -> {
                   if (methodKey?.contains("getOrders") == true) {
                       return ResponseStatusException(
                           HttpStatus.valueOf(response.status()),
                           env.getProperty("order_service.exception.orders_is_empty")
                       )
                   } else RuntimeException("")
               }
               404 -> {
                   if (methodKey?.contains("getOrders") == true) {
                       return ResponseStatusException(HttpStatus.valueOf(response.status()), env.getProperty("order_service.exception.orders_is_empty"))
                   } else RuntimeException("")
               }
               else -> Exception(response.reason())
           }
       } else null
    }
}