package com.example.gestiontienda2.data.remote.firebase.datasource.impl

import com.example.gestiontienda2.data.remote.firebase.models.ProviderFirebase
import com.gestiontienda2.data.remote.firebase.datasource.ProviderFirebaseDataSource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProviderFirebaseDataSourceImpl @Inject constructor() : ProviderFirebaseDataSource {

    private val firestore: FirebaseFirestore.collection("providers")
) : ProviderFirebaseDataSource {

    private val providersCollection = firestore.collection("providers")

    override fun getAllProviders(): Flow<List<ProviderFirebase>> = flow {
        val snapshot = providersCollection.get().await()
        val providers = snapshot.documents.mapNotNull { it.toObject(ProviderFirebase::class.java) }
        emit(providers)
    }.catch { e ->
        // Log or handle the error
        emit(emptyList()) // Emit empty list on error
    }

    override suspend fun getProviderById(providerId: String): ProviderFirebase? {
        return try {
            providersCollection.document(providerId).get().await()
                .toObject(ProviderFirebase::class.java)
        } catch (e: Exception) {
            // Log or handle the error
            null
        }
    }

    override suspend fun addProvider(provider: ProviderFirebase) {
        try {
            providersCollection.document(provider.id).set(provider).await()
        } catch (e: Exception) {
            // Log or handle the error
            throw e // Re-throw or handle as needed
        }
    }

    override suspend fun updateProvider(provider: ProviderFirebase) {
        try {
            providersCollection.document(provider.id).set(provider)
                .await() // Firestore set updates or creates
        } catch (e: Exception) {
            // Log or handle the error
            throw e // Re-throw or handle as needed
        }
    }

    override suspend fun deleteProvider(provider: ProviderFirebase) {
        try {
            providersCollection.document(provider.id).delete().await()
        } catch (e: Exception) {
            // Log or handle the error
            throw e // Re-throw or handle as needed
        }
    }
}