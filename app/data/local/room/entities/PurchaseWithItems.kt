package com.your_app_name.data.local.room.entities

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Representa una compra junto con todos sus ítems asociados.
 *
 * @property purchase La entidad principal de la compra.
 * @property items Lista de ítems que pertenecen a esta compra.
 */
data class PurchaseWithItems(
    @Embedded val purchase: PurchaseEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "purchaseId"
    )
    val items: List<PurchaseItemEntity>
)
