package com.example.catalogservice.jpa

import com.example.catalogservice.dto.CatalogDto
import jakarta.persistence.*
import org.hibernate.annotations.ColumnDefault
import java.io.Serializable
import java.time.Instant

@Entity
@Table(name = "catalog")
data class CatalogEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "product_id", unique = true)
    val productId: String = "",

    @Column(name = "product_name")
    val productName: String = "",

    val stock: Int? = null,

    @Column(name = "unit_price")
    val unitPrice: Int? = null,

    @Column(name="created_at")
    val createdAt: Instant? = Instant.now(),
): Serializable {
    fun toDto() = CatalogDto(
        productId = productId,
        qty = 0,
        unitPrice = 0,
        totalPrice = 0,
        orderId = "",
        userId = "",
    )
}

//fun CatalogDto.toEntity(userId: String, pw: String) = CatalogEntity(
//    id = null,
//    email = email,
//    name = name,
//    userId = userId,
//    encryptedPwd = pw,
//    createdAt = Instant.now()
//
//)
