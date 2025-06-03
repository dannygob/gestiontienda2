package com.example.gestiontienda2.data.local.room.entities

import androidx.room.Embedded
import androidx.room.Relation
import com.example.gestiontienda2.data.local.room.entities.PurchaseEntity
import com.example.gestiontienda2.data.local.room.entities.PurchaseItemEntity

data class PurchaseWithItems(
    @Embedded val purchase: PurchaseEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "purchaseId"
    )
    val items: List<PurchaseItemEntity>
)