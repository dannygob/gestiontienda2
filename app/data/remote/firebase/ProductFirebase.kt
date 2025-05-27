package com.yourcompany.app.data.remote.firebase

import com.google.firebase.firestore.DocumentId
import com.yourcompany.app.domain.models.Product

data class ProductFirebase(
    @DocumentId
    val id: Int = 0,
    val name: String = "",
    val barcode: String = "",
    val purchasePrice: Double = 0.0,
    val salePrice: Double = 0.0,
    val category: String = "",
    val stock: Int = 0,
    val providerId: Int = 0
)

fun ProductFirebase.toDomain(): Product {
    return Product(
        id = this.id,
        name = this.name,
        barcode = this.barcode,
        purchasePrice = this.purchasePrice,
        salePrice = this.salePrice,
        category = this.category,
        stock = this.stock,
        providerId = this.providerId
    )
}

fun Product.toFirebase(): ProductFirebase {
    return ProductFirebase(
        id = this.id,
        name = this.name,
        barcode = this.barcode,
        purchasePrice = this.purchasePrice, // Assuming you want to store purchase price in Firebase
        salePrice = this.salePrice, // Assuming you want to store sale price in Firebase
        category = this.category, // Assuming you want to store category in Firebase
        stock = this.stock, // Assuming you want to store stock in Firebase
        providerId = this.providerId // Assuming you want to store provider ID in Firebase
    )
}