package com.example.gestiontienda2.data.mapper


import com.example.gestiontienda2.data.local.entities.entity.ProductEntity
import com.example.gestiontienda2.domain.models.Product

// Room <-> Domain
fun ProductEntity.toDomain(): Product = Product(
    id = id,
    name = name,
    price = price,
    stock = stock,
    barcode = TODO(),
    purchasePrice = TODO(),
    salePrice = TODO(),
    category = TODO(),
    providerId = TODO(),
    stockQuantity = TODO(),
    description = TODO(),
    reservedStockQuantity = TODO(),
    availableStock = TODO(),
    buyingPrice = TODO(),
    sellingPrice = TODO(),
    imageUrl = TODO()
)

fun Product.toEntity(): ProductEntity = ProductEntity(
    id = id,
    name = name,
    price = price,
    stock = stock,
    barcode = TODO(),
    purchasePrice = TODO(),
    salePrice = TODO(),
    category = TODO(),
    stockQuantity = TODO(),
    reservedStockQuantity = TODO(),
    providerId = TODO(),
    location = TODO(),
    description = TODO()
)

// Firebase <-> Domain
fun FirebaseProduct.toDomain(): Product = Product(
    id = id,
    name = name,
    price = price,
    stock = stock,
    barcode = TODO(),
    purchasePrice = TODO(),
    salePrice = TODO(),
    category = TODO(),
    providerId = TODO(),
    stockQuantity = TODO(),
    description = TODO(),
    reservedStockQuantity = TODO(),
    availableStock = TODO(),
    buyingPrice = TODO(),
    sellingPrice = TODO(),
    imageUrl = TODO()
)

fun Product.toFirebase(): FirebaseProduct = FirebaseProduct(
    id = id,
    name = name,
    price = price,
    stock = stock
)