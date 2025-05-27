package com.yourcompany.app.data.remote.firebase

import com.google.firebase.firestore.DocumentId
import com.yourcompany.app.domain.models.ServiceExpense // Assuming your domain models are in this package

data class ServiceExpenseFirebase(
    @DocumentId
    val id: String = "",
    val type: String = "",
    val amount: Double = 0.0,
    val date: Long? = 0L, // Make nullable for Firebase compatibility
    val description: String = ""
)

fun ServiceExpenseFirebase.toDomainModel(): ServiceExpense {
    return ServiceExpense(
        id = this.id,
        type = this.type,
        amount = this.amount,
        date = this.date ?: 0L, // Handle nullable date
        description = this.description
    )
}

fun ServiceExpense.toFirebaseModel(): ServiceExpenseFirebase {
    return ServiceExpenseFirebase(
        id = this.id,
        type = this.type,
        amount = this.amount,
        date = this.date,
        description = this.description
    )
}