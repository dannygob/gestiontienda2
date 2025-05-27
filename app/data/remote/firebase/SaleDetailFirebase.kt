package com.yourcompany.app.data.remote.firebase

import com.yourcompany.app.domain.models.SaleDetail

data class SaleDetailFirebase(
    val id: Int = 0,
    val saleId: Int = 0,
    val productId: Int = 0,
    val quantity: Int = 0,
    val unitPrice: Double = 0.0
)

fun SaleDetailFirebase.toDomainModel(): SaleDetail {
    return SaleDetail(
        id = this.id,
        saleId = this.saleId,
        productId = this.productId,
        quantity = this.quantity,
        unitPrice = this.unitPrice
    )
}

fun SaleDetail.toFirebaseModel(): SaleDetailFirebase {
    return SaleDetailFirebase(
        id = this.id,
        saleId = this.saleId,
        productId = this.productId,
        quantity = this.quantity,
        unitPrice = this.unitPrice
    )
}