package com.example.gestiontienda2.domain.models

data class OperationalExpense(
    val id: Int = 0,
    val description: String,
    val date: String, // Consider changing to Long or a Date type in a future refactor if needed
    val amount: Double, // Domain model uses Double
    val category: String,
    val notes: String?,
    val type: String // e.g., "service", "expense"
)