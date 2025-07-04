package com.example.gestiontienda2.data.repository

import androidx.room.Embedded
import androidx.room.Relation
import com.example.gestiontienda2.data.local.entities.entity.SaleEntity
import com.example.gestiontienda2.data.local.entities.entity.SaleItemEntity


data class SaleWithItems(
    @Embedded val sale: SaleEntity,
    @Relation(
        parentColumn = "id", // The column in the parent entity (SaleEntity)
        entityColumn = "saleId" // The column in the child entity (SaleItemEntity) that references the parent
    )
    val items: List<SaleItemEntity>,
)