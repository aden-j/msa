package com.example.catalogservice.service

import com.example.catalogservice.jpa.CatalogEntity

interface CatalogService {
    fun getAllCatalogs(): Iterable<CatalogEntity>
}