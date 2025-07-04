package com.example.gestiontienda2.data.repository

import androidx.room.Embedded
import androidx.room.Relation
import com.example.gestiontienda2.data.local.entities.entity.PurchaseEntity
import com.example.gestiontienda2.data.local.entities.entity.PurchaseItemEntity


data class PurchaseWithItems(
    @Embedded val purchase: PurchaseEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "purchaseId"
    )
    val items: List<PurchaseItemEntity>
)