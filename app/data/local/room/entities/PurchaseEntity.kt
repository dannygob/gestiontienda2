package com.your_app_name.data.local.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Representa una compra realizada a un proveedor.
 *
 * @property id Identificador único de la compra (autogenerado por Room).
 * @property providerId ID del proveedor asociado a la compra.
 * @property purchaseDate Fecha de la compra en formato timestamp (milisegundos desde epoch).
 * @property totalAmount Monto total de la compra.
 */
@Entity(tableName = "purchases")
data class PurchaseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val providerId: Int,

    val purchaseDate: Long,

    val totalAmount: Double
)
