package com.yourcompany.app.data.remote.firebase

import com.google.firebase.firestore.DocumentId
import com.yourcompany.app.domain.models.Order

data class OrderFirebase(
    @DocumentId
    val id: Int = 0,
    val clientId: Int = 0,
    val date: Long = 0L,
    val state: String = "",
    val total: Double = 0.0
)

fun OrderFirebase.toDomain(): Order {
    return Order(
        id = id,
        clientId = clientId,
        date = date,
        state = state,
        total = total
    )
}

fun Order.toFirebase(): OrderFirebase {
    return OrderFirebase(
        id = id,
        clientId = clientId,
        date = date,
        state = state,
        total = total
    )
}