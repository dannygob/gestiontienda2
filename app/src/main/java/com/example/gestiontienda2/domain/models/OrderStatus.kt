package com.example.gestiontienda2.domain.models

data class OrderStatus (
    val id: Int = 0,
    val name: String = "",
    val description: String? = null,
    val color: String? = null,
    val icon: String? = null,
    val isActive: Boolean = true
) {

    companion object {
        const val PENDING = 1
        const val IN_PROGRESS = 2
        const val COMPLETED = 3
        const val CANCELLED = 4

        fun getDefaultStatus() = OrderStatus(
            id = PENDING,
            name = "Pending",
            description = "Order is pending",
            color = "#FF0000", // Red color for pending
            icon = "ic_pending"
)

    }

}
