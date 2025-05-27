package com.yourcompany.yourapp.domain.models

data class Provider(
    // Added id field
    val id: Int = 0,
    val name: String,
    val phone: String,
    // Added email field
    val address: String,
    val email: String? // Made email nullable
)