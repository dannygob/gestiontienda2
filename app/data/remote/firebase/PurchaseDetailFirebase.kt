package com.yourcompany.yourapp.data.remote.firebase

import com.google.firebase.firestore.DocumentId

import com.yourcompany.yourapp.domain.models.PurchaseDetail

data class PurchaseDetailFirebase(
    @DocumentId
    val id: Int = 0,
    val purchaseId: Int = 0,
    val productId: Int = 0,
    val quantity: Int = 0,
    val unitPrice: Double = 0.0
)

fun PurchaseDetailFirebase.toDomainModel(): PurchaseDetail {
    return PurchaseDetail(
        id = this.id,
        purchaseId = this.purchaseId,
        productId = this.productId,
        quantity = this.quantity,
        unitPrice = this.unitPrice
    )
}

fun PurchaseDetail.toFirebaseModel(): PurchaseDetailFirebase {
    return PurchaseDetailFirebase(
        id = this.id,