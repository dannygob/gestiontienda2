package com.yourcompany.app.domain.repository

import com.yourcompany.app.domain.models.ServiceExpense
import kotlinx.coroutines.flow.Flow

interface ServiceExpenseRepository {

    fun getAllServiceExpenses(): Flow<List<ServiceExpense>>

    fun getServiceExpenseById(id: Int): Flow<ServiceExpense?>

    suspend fun insertServiceExpense(serviceExpense: ServiceExpense)

    suspend fun updateServiceExpense(serviceExpense: ServiceExpense)

    suspend fun deleteServiceExpense(serviceExpense: ServiceExpense)

    suspend fun getTotalServiceExpenseAmount(startDate: Long? = null, endDate: Long? = null): Double

    fun getServiceExpensesByDateRange(startDate: Long? = null, endDate: Long? = null): Flow<List<ServiceExpense>>
}