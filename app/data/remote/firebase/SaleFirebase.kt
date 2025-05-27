package com.yourcompany.app.data.remote.firebase

import com.google.firebase.firestore.DocumentId
import com.yourcompany.app.domain.models.Sale

data class SaleFirebase(
    @DocumentId
    val id: Int = 0,
    val date: Long = 0L,
    val clientId: Int = 0,
    val total: Double = 0.0
)

fun SaleFirebase.toDomain(): Sale {
    return Sale(id = id, date = date, clientId = clientId, total = total)
}

fun Sale.toFirebase(): SaleFirebase {
    return SaleFirebase(id = id, date = date, clientId = clientId, total = total)
}