package com.example.gestiontienda2.data.remote.firebase.datasource

import com.gestiontienda2.data.remote.firebase.models.SaleFirebase
import kotlinx.coroutines.flow.Flow

interface SaleFirebaseDataSource {

    fun getSales(): Flow<List<SaleFirebase>>

    suspend fun getSaleById(saleId: String): SaleFirebase?

    suspend fun addSale(sale: SaleFirebase)

    suspend fun updateSale(sale: SaleFirebase)

    suspend fun deleteSale(saleId: String)
}