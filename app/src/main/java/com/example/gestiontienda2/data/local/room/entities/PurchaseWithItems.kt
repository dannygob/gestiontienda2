package com.gestiontienda2.data.local.room.entities

import androidx.room.Embedded
import androidx.room.Relation

data class PurchaseWithItems(
    @Embedded val purchase: PurchaseEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "purchaseId"
    )
    val items: List<PurchaseItemEntity>
)