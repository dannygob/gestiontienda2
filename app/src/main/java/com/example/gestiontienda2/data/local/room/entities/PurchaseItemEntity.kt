package com.your_app_name.data.local.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Representa un ítem dentro de una compra.
 *
 * @property id Identificador único del ítem (autogenerado).
 * @property purchaseId ID de la compra a la que pertenece este ítem.
 * @property productId ID del producto comprado.
 * @property quantity Cantidad comprada del producto.
 * @property purchasePrice Precio unitario del producto en la compra.
 */
@Entity(
    tableName = "purchase_items",
    foreignKeys = [ForeignKey(
        entity = PurchaseEntity::class,
        parentColumns = ["id"],
        childColumns = ["purchaseId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class PurchaseItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(index = true)
    val purchaseId: Int,

    val productId: Int,

    val quantity: Int,

    val purchasePrice: Double
)
