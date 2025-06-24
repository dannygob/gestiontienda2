package com.example.gestiontienda2.data.remote.firebase.models

import com.google.firebase.firestore.PropertyName

data class ProductFirebase(
    var id: String = "", // Typically String for Firebase doc ID
    var name: String = "",
    var price: Double = 0.0, // General price
    var barcode: String = "",
    @get:PropertyName("purchase_price") @set:PropertyName("purchase_price") // Example for Firestore field naming
    var purchasePrice: Double = 0.0,
    @get:PropertyName("sale_price") @set:PropertyName("sale_price")
    var salePrice: Double = 0.0,
    var category: String = "",
    var stock: Int = 0,
    @get:PropertyName("provider_id") @set:PropertyName("provider_id")
    var providerId: Int = 0, // Assuming Int, could be String if provider IDs in Firebase are strings
    @get:PropertyName("stock_quantity") @set:PropertyName("stock_quantity")
    var stockQuantity: Int = 0,
    var description: String? = null,
    @get:PropertyName("reserved_stock_quantity") @set:PropertyName("reserved_stock_quantity")
    var reservedStockQuantity: Int = 0,
    @get:PropertyName("available_stock") @set:PropertyName("available_stock")
    var availableStock: Int = 0
) {
    // Default constructor for Firebase deserialization
    constructor() : this("", "", 0.0, "", 0.0, 0.0, "", 0, 0, 0, null, 0, 0)
}
