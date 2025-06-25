package com.your_app_name.data.remote.firebase.models

data class ServiceExpenseFirebase(
    val id: String = "",
    val description: String = "",
    val date: Long = 0L,
    val amount: Double = 0.0,
    val category: String = "",
    val notes: String? = null
)