package com.yourcompany.app.data.remote.firebase

import com.google.firebase.firestore.DocumentId
import com.yourcompany.app.domain.models.Provider

data class ProviderFirebase(
    @DocumentId
    val id: Int = 0,
    val name: String = "",
    val phone: String = "",
    val address: String = "",
    val email: String = ""
)

fun ProviderFirebase.toDomain(): Provider {
    return Provider(
        id = id,
        name = name,
        phone = phone,
        address = address,
        email = email
    )
}

fun Provider.toFirebase(): ProviderFirebase {
    return ProviderFirebase(
        id = id,
        name = name,
        phone = phone,
        address = address,
        email = email
    )
}