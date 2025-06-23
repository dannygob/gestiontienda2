package com.example.gestiontienda2.data.remote.firebase.models

data class ProductFirebase(
    val id: Int,
    val name: String,
    val price: Double,
    val stock: Int,
    // ...outros campos
)