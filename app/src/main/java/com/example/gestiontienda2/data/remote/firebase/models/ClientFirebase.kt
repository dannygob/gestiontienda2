package com.example.gestiontienda2.data.remote.firebase.models


data class ClientFirebase(
    val id: String = "",
    val name: String = "",
    val phone: String? = null,
    val address: String? = null,
    val email: String? = null,
    val paymentPreference: String? = null,
)
