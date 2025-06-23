package com.example.gestiontienda2.data.local.entities.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val name: String,
    val barcode: String,
    val purchasePrice: Double,
    val salePrice: Double,
    val category: String,
    val stockQuantity: Int,
    val reservedStockQuantity: Int = 0,
    val providerId: Long?, // Puede ser null si aún no hay proveedor asignado
    val location: String?, // Ej: "Pasillo 1 - Estante 2 - Nivel 3"
    val description: String? = null,
    val location1: String,
    val price: Double,
    val stock: Int,
)