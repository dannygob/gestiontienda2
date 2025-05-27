package com.example.minishop.data.remote.firebase

import com.example.minishop.data.remote.firebase.SaleFirebase

interface SaleFirebaseDataSource {
    suspend fun getSales(): List<SaleFirebase>
    suspend fun getSaleById(saleId: String): SaleFirebase?
    suspend fun addSale(sale: SaleFirebase)
    suspend fun updateSale(sale: SaleFirebase)
    suspend suspend fun deleteSale(saleId: String)
}