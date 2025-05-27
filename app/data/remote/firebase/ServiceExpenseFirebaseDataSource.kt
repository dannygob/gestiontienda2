package com.example.app.data.remote.firebase

import com.example.app.data.remote.firebase.ServiceExpenseFirebase

interface ServiceExpenseFirebaseDataSource {
    suspend fun getServiceExpense(id: String): ServiceExpenseFirebase?
    suspend fun getAllServiceExpenses(): List<ServiceExpenseFirebase>
    suspend fun addServiceExpense(serviceExpense: ServiceExpenseFirebase)
    suspend fun updateServiceExpense(serviceExpense: ServiceExpenseFirebase)
    suspend fun deleteServiceExpense(id: String)
}