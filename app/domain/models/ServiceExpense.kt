package com.your_app_name.domain.models

data class ServiceExpense(
    val id: Int,
    val type: String,
    val description: String,
    val date: Long,
    val amount: Double,
    val category: String,
    val notes: String? = null
)