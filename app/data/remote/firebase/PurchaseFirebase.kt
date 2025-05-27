package com.your_app_name.data.remote.firebase

import com.google.firebase.firestore.DocumentId
import com.your_app_name.domain.models.Purchase

data class PurchaseFirebase(
    @DocumentId
    val id: String = "", // Firestore uses String for document IDs
    val date: Long = 0L,
    val providerId: Int = 0,
    val total: Double = 0.0
)

fun PurchaseFirebase.toDomainModel(): Purchase {
    return Purchase(
        id = this.id.toIntOrNull() ?: 0, // Convert String ID to Int for domain model
        date = this.date,
        providerId = this.providerId,
        total = this.total
    )
}

fun Purchase.toFirebaseModel(): PurchaseFirebase {
    return PurchaseFirebase(
        id = this.id.toString(), // Convert Int ID to String for Firebase
        date = this.date,
        providerId = this.providerId,
        total = this.total
    )
}