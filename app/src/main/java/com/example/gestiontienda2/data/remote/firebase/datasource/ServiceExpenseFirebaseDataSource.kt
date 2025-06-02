package app.data.remote.firebase.datasource

import app.data.remote.firebase.models.ServiceExpenseFirebase
import kotlinx.coroutines.flow.Flow

interface ServiceExpenseFirebaseDataSource {
    fun getServiceExpenses(): Flow<List<ServiceExpenseFirebase>>
    suspend fun getServiceExpenseById(serviceExpenseId: String): ServiceExpenseFirebase?
    suspend fun addServiceExpense(serviceExpense: ServiceExpenseFirebase)
    suspend fun updateServiceExpense(serviceExpense: ServiceExpenseFirebase)
    suspend fun deleteServiceExpense(serviceExpenseId: String)
}