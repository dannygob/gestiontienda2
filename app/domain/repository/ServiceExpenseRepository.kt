package com.yourcompany.yourapp.domain.repository

import com.yourcompany.yourapp.domain.models.ServiceExpense
import kotlinx.coroutines.flow.Flow

interface ServiceExpenseRepository {

    fun getAllServiceExpenses(): Flow<List<ServiceExpense>>

    fun getServiceExpenseById(id: Int): Flow<ServiceExpense?>

    suspend fun insertServiceExpense(serviceExpense: ServiceExpense)

    suspend fun updateServiceExpense(serviceExpense: ServiceExpense)

    suspend fun deleteServiceExpense(serviceExpense: ServiceExpense)
}