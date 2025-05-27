package com.your_app_name.data.local.room.entities

import androidx.room.Embedded
import androidx.room.Relation

data class SaleWithItems(
    @Embedded val sale: SaleEntity,
    @Relation(
        parentColumn = "id", // The column in the parent entity (SaleEntity)
        entityColumn = "saleId" // The column in the child entity (SaleItemEntity) that references the parent
    )
    val items: List<SaleItemEntity>
)