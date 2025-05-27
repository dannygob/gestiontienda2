package com.your_app_name.data.remote.firebase.datasource.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.your_app_name.data.remote.firebase.datasource.ServiceExpenseFirebaseDataSource
import com.your_app_name.data.remote.firebase.models.ServiceExpenseFirebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ServiceExpenseFirebaseDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ServiceExpenseFirebaseDataSource {

    private val serviceExpensesCollection = firestore.collection("serviceExpenses")

    override fun getServiceExpenses(): Flow<List<ServiceExpenseFirebase>> {
        return serviceExpensesCollection.snapshots().map { querySnapshot ->
            querySnapshot.documents.mapNotNull { documentSnapshot ->
                documentSnapshot.toObject(ServiceExpenseFirebase::class.java)?.copy(id = documentSnapshot.id)
            }
        }
    }

    override suspend fun getServiceExpenseById(serviceExpenseId: String): ServiceExpenseFirebase? {
        return try {
            serviceExpensesCollection.document(serviceExpenseId).get().await()
                .toObject(ServiceExpenseFirebase::class.java)?.copy(id = serviceExpenseId)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun addServiceExpense(serviceExpense: ServiceExpenseFirebase) {
        try {
            // Firebase automatically generates an ID if you don't provide one in the document() call
            serviceExpensesCollection.add(serviceExpense).await()
        } catch (e: Exception) {
            e.printStackTrace()
            throw e // Re-throw to allow use cases/repositories to handle
        }
    }

    override suspend fun updateServiceExpense(serviceExpense: ServiceExpenseFirebase) {
        try {
            serviceExpensesCollection.document(serviceExpense.id).set(serviceExpense).await()
        } catch (e: Exception) {
            e.printStackTrace()
            throw e // Re-throw to allow use cases/repositories to handle
        }
    }

    override suspend fun deleteServiceExpense(serviceExpenseId: String) {
        try {
            serviceExpensesCollection.document(serviceExpenseId).delete().await()
        } catch (e: Exception) {
            e.printStackTrace()
            throw e // Re-throw to allow use cases/repositories to handle
        }
    }
}