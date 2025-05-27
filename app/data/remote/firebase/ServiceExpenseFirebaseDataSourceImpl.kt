package com.example.mystoreapp.data.remote.firebase

import com.example.mystoreapp.data.remote.firebase.model.ServiceExpenseFirebase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ServiceExpenseFirebaseDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ServiceExpenseFirebaseDataSource {

    private val serviceExpensesCollection = firestore.collection("serviceExpenses")

    override suspend fun getServiceExpenses(): List<ServiceExpenseFirebase> {
        return try {
            serviceExpensesCollection.get().await().toObjects(ServiceExpenseFirebase::class.java)
        } catch (e: Exception) {
            // Handle exceptions (e.g., network errors, permission denied)
            emptyList()
        }
    }

    override suspend fun getServiceExpenseById(id: String): ServiceExpenseFirebase? {
        return try {
            serviceExpensesCollection.document(id).get().await().toObject(ServiceExpenseFirebase::class.java)
        } catch (e: Exception) {
            // Handle exceptions
            null
        }
    }

    override suspend fun addServiceExpense(serviceExpense: ServiceExpenseFirebase) {
        try {
            serviceExpensesCollection.add(serviceExpense).await()
        } catch (e: Exception) {
            // Handle exceptions
        }
    }

    override suspend fun updateServiceExpense(serviceExpense: ServiceExpenseFirebase) {
        try {
            serviceExpensesCollection.document(serviceExpense.id).set(serviceExpense).await()
        } catch (e: Exception) {
            // Handle exceptions
        }
    }

    override suspend fun deleteServiceExpense(id: String) {
        try {
            serviceExpensesCollection.document(id).delete().await()
        } catch (e: Exception) {
            // Handle exceptions
        }
    }
}