package com.yourcompany.yourappname.data.remote.firebase

import com.google.firebase.firestore.DocumentId

import com.yourcompany.yourappname.domain.models.Client

data class ClientFirebase(
    @DocumentId
    val id: Int = 0,
    val name: String = "",
    val phone: String = "",
    val address: String = "",
    val email: String = ""
)

fun ClientFirebase.toDomainModel(): Client {
    return Client(
        id = this.id,
        name = this.name,
        phone = this.phone,
        address = this.address,
        email = this.email
        // Note: Preferences field from domain model is not in Firebase model for now
    )
}

fun Client.toFirebaseModel(): ClientFirebase {
    return ClientFirebase(id = this.id, name = this.name, phone = this.phone, address = this.address, email = this.email)
}