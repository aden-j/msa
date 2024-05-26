package com.example.catalogservice.controller

import com.example.catalogservice.jpa.CatalogEntity
import com.example.catalogservice.service.CatalogService
import com.example.catalogservice.vo.ResponseCatalog
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/catalog-service")
class CatalogController(
    @Autowired val env: Environment,
    @Autowired val catalogService: CatalogService
) {

    @GetMapping("/health-check")
    fun status(): String {
        return "It's working in User Service on PORT ${env.getProperty("local.server.port")}"
    }

    @GetMapping("/catalogs")
    fun getCatalogs(): ResponseEntity<List<ResponseCatalog>> {
        val catalogList = catalogService.getAllCatalogs()
        val result = mutableListOf<ResponseCatalog>()
        catalogList.forEach { it ->
            result.add(it.toModel())
        }
        return ResponseEntity.status(HttpStatus.OK).body(result)
    }

}

fun CatalogEntity.toModel(): ResponseCatalog {
    return ResponseCatalog(
        productId = productId!!,
        productName = productName,
        stock = stock!!,
        unitPrice = unitPrice!!,
        createdAt = createdAt,
    )
}