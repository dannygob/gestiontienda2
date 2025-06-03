package com.example.gestiontienda2.domain.repository

import com.gestiontiendas2.data.domain.models.ServiceExpense
import kotlinx.coroutines.flow.Flow

interface ServiceExpenseRepository {

    fun getAllServiceExpenses(): Flow<List<ServiceExpense>>

    fun getServiceExpenseById(id: Int): Flow<ServiceExpense?>

    suspend fun insertServiceExpense(serviceExpense: ServiceExpense)

    suspend fun updateServiceExpense(serviceExpense: ServiceExpense)

    suspend fun deleteServiceExpense(serviceExpense: ServiceExpense)

    suspend fun getTotalServiceExpenseAmount(startDate: Long? = null, endDate: Long? = null): Double

    fun getServiceExpensesByDateRange(
        startDate: Long? = null,
        endDate: Long? = null
    ): Flow<List<ServiceExpense>>
}