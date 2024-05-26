package com.example.catalogservice.jpa

import org.springframework.data.repository.CrudRepository

interface CatalogRepository: CrudRepository<CatalogEntity, Long> {
    fun findByProductId(productId: String): CatalogEntity?
}