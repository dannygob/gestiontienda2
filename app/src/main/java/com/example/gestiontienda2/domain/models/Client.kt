package com.example.gestiontienda2.domain.models


data class Client(
    // Added id field
    val id: Int = 0,
    val name: String,
    val phone: String,
    // Added email field
    val address: String,
    val email: String? // Made email nullable
)