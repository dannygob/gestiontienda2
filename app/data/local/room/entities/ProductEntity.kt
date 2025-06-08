package com.example.gestiontienda2.data.local.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.gestiontienda2.domain.models.Product

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = "",
    val barcode: String? = null,
    val purchasePrice: Double = 0.0,
    val salePrice: Double = 0.0,
    val category: String? = null,
    val stockQuantity: Int = 0,
    val reservedStockQuantity: Int = 0,
    val providerId: Int? = null
)

fun ProductEntity.toDomain(): Product {
    return Product(
        id = this.id, // Assuming domain Product id is Int
        name = this.name,
        barcode = this.barcode ?: "",
        purchasePrice = this.purchasePrice,
        salePrice = this.salePrice,
        category = this.category ?: "",
        stockQuantity = this.stockQuantity,
        reservedStockQuantity = this.reservedStockQuantity,
        availableStock = this.stockQuantity - this.reservedStockQuantity, // Calculated
        providerId = this.providerId.toString() // Assuming domain providerId is String
    )
}
