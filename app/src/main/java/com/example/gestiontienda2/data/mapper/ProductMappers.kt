package com.example.gestiontienda2.data.mapper

import com.example.gestiontienda2.data.local.entities.entity.ProductEntity
import com.example.gestiontienda2.data.remote.firebase.models.ProductFirebase
import com.example.gestiontienda2.domain.models.Product

// ---------------------
// Room <-> Domain
// ---------------------

fun ProductEntity.toDomain(): Product = Product(
    id = this.id.toInt(),
    name = this.name,
    description = this.description ?: "",
    price = this.salePrice,
    stockQuantity = this.stockQuantity,

    salePrice = this.salePrice,
    category = this.category,
    purchasePrice = this.purchasePrice,
    stock = this.stockQuantity - this.reservedStockQuantity,
    barcode = this.barcode,
    providerId = this.providerId?.toInt() ?: 0,

    buyingPrice = 0.0,
    sellingPrice = 0.0,
    imageUrl = ""
)

fun Product.toEntity(): ProductEntity = ProductEntity(
    id = this.id.toLong(),
    name = this.name,
    barcode = this.barcode,
    purchasePrice = this.purchasePrice,
    salePrice = this.salePrice,
    category = this.category,
    stockQuantity = this.stockQuantity,

    providerId = this.providerId.toLong(),
    location = null,
    description = this.description ?: "",
    location1 = "",
    price = this.price,
    stock = this.stock
)

// ---------------------
// Firebase <-> Domain
// ---------------------

fun ProductFirebase.toDomain(): Product = Product(
    id = this.id,
    name = this.name,
    price = this.price,
    stock = this.stock,
    barcode = "",
    purchasePrice = 0.0,
    salePrice = 0.0,
    category = "",
    providerId = 0,
    stockQuantity = 0,
    description = "",
    buyingPrice = 0.0,
    sellingPrice = 0.0,
    imageUrl = ""
)

fun Product.toFirebase(): ProductFirebase = ProductFirebase(
    id = this.id,
    name = this.name,
    price = this.price,
    stock = this.stock
)
