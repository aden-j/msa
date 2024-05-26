package com.example.catalogservice.service

import com.example.catalogservice.jpa.CatalogEntity
import com.example.catalogservice.jpa.CatalogRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CatalogServiceImpl(
    @Autowired val catalogRepository: CatalogRepository
): CatalogService {
    override fun getAllCatalogs(): Iterable<CatalogEntity> {
        return catalogRepository.findAll()
    }
}