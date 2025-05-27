package com.yourcompany.yourapp.domain.repository

import com.yourcompany.yourapp.domain.models.ServiceExpense
import kotlinx.coroutines.flow.Flow

interface ServiceExpenseRepository {

    fun getServiceExpenses(): Flow<List<ServiceExpense>>

    suspend fun getServiceExpenseById(serviceExpenseId: Int): ServiceExpense?

    suspend fun addServiceExpense(serviceExpense: ServiceExpense): Long

    suspend fun updateServiceExpense(serviceExpense: ServiceExpense)

    suspend fun deleteServiceExpense(serviceExpense: ServiceExpense)
}